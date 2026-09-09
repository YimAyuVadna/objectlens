package com.example.ml

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageProxy
import com.example.data.local.LabelTier
import com.example.data.local.ObjectKnowledgeBase
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.math.max
import kotlin.math.min
import com.google.mlkit.vision.objects.DetectedObject as MlKitDetectedObject

class ObjectDetectorEngine {

    private val _isModelLoaded = MutableStateFlow(false)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded.asStateFlow()

    private val _inferenceFps = MutableStateFlow(0)
    val inferenceFps: StateFlow<Int> = _inferenceFps.asStateFlow()

    private val _inferenceLatencyMs = MutableStateFlow(0L)
    val inferenceLatencyMs: StateFlow<Long> = _inferenceLatencyMs.asStateFlow()

    // Smooth tracking state: trackingId -> TrackedObject (with grace period)
    private val activeTracks = mutableMapOf<Int, TrackedObject>()
    private var nextTrackingId = 1
    private var lastProcessTime = 0L
    private var lastAnalyzedFrameTime = 0L

    // Configurable target frame rate mode ("Low" ~15 FPS, "Medium" ~30 FPS, "High" ~60 FPS / uncapped)
    var targetFpsMode: String = "High"

    // Dedicated background executor for ML Kit pipeline to keep UI smooth and prevent main-thread blocking
    private val backgroundExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    // ML Kit detectors
    private var mlkitStreamDetector: ObjectDetector? = null
    private var mlkitLabeler: ImageLabeler? = null

    // Self-learning engine and visual memory manager
    var labelMemory: LabelMemory? = null
    var learnedObjectManager: LearnedObjectManager? = null

    // Classification cache for fast 30 FPS tracking: trackingId -> CachedLabel
    private data class CachedClassification(
        val label: String,
        val displayName: String,
        val category: String,
        val confidence: Float,
        val isLearned: Boolean,
        val timestamp: Long
    )
    private val classificationCache = ConcurrentHashMap<Int, CachedClassification>()

    // Latest cropped bitmaps of active detections: detectionId -> Bitmap
    private val activeCropMap = ConcurrentHashMap<String, Bitmap>()

    private data class TrackedObject(
        val detection: DetectedObject,
        val missedFrames: Int = 0
    )

    companion object {
        private const val TRACK_GRACE_FRAMES = 3
        private const val CACHE_EXPIRATION_MS = 800L
    }

    suspend fun initialize() = withContext(Dispatchers.Default) {
        val streamOptions = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        mlkitStreamDetector = ObjectDetection.getClient(streamOptions)

        val labelerOptions = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.20f)
            .build()
        mlkitLabeler = ImageLabeling.getClient(labelerOptions)

        _isModelLoaded.value = true
    }

    /**
     * Clears cached classifications and active crops so live detections immediately re-evaluate against updated memory.
     */
    fun clearClassificationCache() {
        classificationCache.clear()
        activeCropMap.clear()
    }

    /**
     * Gets the latest crop bitmap for a detected object (used for teaching/renaming).
     */
    fun getCropForObject(obj: DetectedObject): Bitmap? {
        return activeCropMap[obj.id]
    }

    /**
     * Processes a live camera frame through ML Kit Object Detection with crop-based labeling
     * and on-device visual memory matching.
     */
    fun processLiveFrame(
        imageProxy: ImageProxy,
        minConfidence: Float,
        isFrontCamera: Boolean = false,
        onResult: (List<DetectedObject>) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val detector = mlkitStreamDetector
        val labeler = mlkitLabeler
        if (detector == null || labeler == null) {
            imageProxy.close()
            return
        }

        val rotation = imageProxy.imageInfo.rotationDegrees
        val inputImage = try {
            InputImage.fromMediaImage(mediaImage, rotation)
        } catch (_: Exception) {
            imageProxy.close()
            return
        }

        val imageWidth: Int
        val imageHeight: Int
        if (rotation == 90 || rotation == 270) {
            imageWidth = imageProxy.height
            imageHeight = imageProxy.width
        } else {
            imageWidth = imageProxy.width
            imageHeight = imageProxy.height
        }

        val minIntervalMs = when (targetFpsMode) {
            "Low" -> 66L      // ~15 FPS power saver
            "Medium" -> 33L   // ~30 FPS standard
            else -> 0L        // 60 FPS / Uncapped
        }

        val currentTime = System.currentTimeMillis()
        if (minIntervalMs > 0L && (currentTime - lastAnalyzedFrameTime) < minIntervalMs) {
            imageProxy.close()
            return
        }
        lastAnalyzedFrameTime = currentTime

        val frameStartTime = System.currentTimeMillis()

        try {
            detector.process(inputImage)
                .addOnCompleteListener(backgroundExecutor) { detectTask ->
                    try {
                        val mlkitObjects = if (detectTask.isSuccessful) detectTask.result else emptyList()

                    // Lazy bitmap conversion: only decode & rotate if needed
                    var lazyOrientedBitmap: Bitmap? = null
                    var attemptedBitmapConversion = false

                    fun getOrientedBitmap(): Bitmap? {
                        if (!attemptedBitmapConversion) {
                            attemptedBitmapConversion = true
                            val rawBitmap = try {
                                if (imageProxy.image != null) imageProxy.toBitmap() else null
                            } catch (_: Exception) {
                                null
                            }
                            lazyOrientedBitmap = if (rawBitmap != null && rotation != 0) {
                                val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
                                try {
                                    Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                                } catch (_: Exception) {
                                    rawBitmap
                                }
                            } else {
                                rawBitmap
                            }
                        }
                        return lazyOrientedBitmap
                    }

                    val now = System.currentTimeMillis()
                    val frameDetections = mutableListOf<DetectedObject>()

                    // 1. Process localized bounding boxes from ML Kit Object Detector
                    for (obj in mlkitObjects) {
                        val box = obj.boundingBox
                        val trackId = obj.trackingId ?: nextTrackingId++
                        val detectionId = "live_$trackId"

                        // Check classification cache
                        val cached = classificationCache[trackId]

                        val rawLeft = box.left.toFloat() / imageWidth
                        val rawRight = box.right.toFloat() / imageWidth
                        val normalizedLeft = if (isFrontCamera) 1f - rawRight else rawLeft
                        val normalizedRight = if (isFrontCamera) 1f - rawLeft else rawRight
                        val normalizedTop = box.top.toFloat() / imageHeight
                        val normalizedBottom = box.bottom.toFloat() / imageHeight

                        if (cached != null && (now - cached.timestamp < CACHE_EXPIRATION_MS)) {
                            // Fast path: use cached classification for smooth tracking
                            frameDetections.add(
                                DetectedObject(
                                    id = detectionId,
                                    label = cached.label,
                                    displayName = cached.displayName,
                                    confidence = cached.confidence,
                                    category = cached.category,
                                    normalizedLeft = normalizedLeft.coerceIn(0.02f, 0.95f),
                                    normalizedTop = normalizedTop.coerceIn(0.02f, 0.95f),
                                    normalizedRight = normalizedRight.coerceIn(0.05f, 0.98f),
                                    normalizedBottom = normalizedBottom.coerceIn(0.05f, 0.98f),
                                    trackingId = trackId,
                                    isLearned = cached.isLearned
                                )
                            )
                        } else {
                            val orientedBitmap = getOrientedBitmap()
                            if (orientedBitmap != null && !orientedBitmap.isRecycled) {
                                // Extract crop for precision classification and visual memory
                                val cropLeft = box.left.coerceIn(0, orientedBitmap.width - 1)
                                val cropTop = box.top.coerceIn(0, orientedBitmap.height - 1)
                                val cropW = box.width().coerceIn(10, orientedBitmap.width - cropLeft)
                                val cropH = box.height().coerceIn(10, orientedBitmap.height - cropTop)

                                val cropBitmap = try {
                                    Bitmap.createBitmap(orientedBitmap, cropLeft, cropTop, cropW, cropH)
                                } catch (_: Exception) {
                                    null
                                }

                                if (cropBitmap != null) {
                                    activeCropMap[detectionId] = cropBitmap

                                    // Step 1: Check On-Device Visual Memory ONLY for user-taught custom objects
                                    val userMatch = learnedObjectManager?.findUserTaughtMatch(cropBitmap)

                                    if (userMatch != null) {
                                        val entity = userMatch.entity
                                        val conf = userMatch.similarity.coerceIn(0.75f, 0.99f)

                                        classificationCache[trackId] = CachedClassification(
                                            label = entity.name.lowercase(),
                                            displayName = entity.name,
                                            category = entity.category,
                                            confidence = conf,
                                            isLearned = true,
                                            timestamp = now
                                        )

                                        frameDetections.add(
                                            DetectedObject(
                                                id = detectionId,
                                                label = entity.name.lowercase(),
                                                displayName = entity.name,
                                                confidence = conf,
                                                category = entity.category,
                                                normalizedLeft = normalizedLeft.coerceIn(0.02f, 0.95f),
                                                normalizedTop = normalizedTop.coerceIn(0.02f, 0.95f),
                                                normalizedRight = normalizedRight.coerceIn(0.05f, 0.98f),
                                                normalizedBottom = normalizedBottom.coerceIn(0.05f, 0.98f),
                                                trackingId = trackId,
                                                isLearned = true
                                            )
                                        )

                                        learnedObjectManager?.recordRecognition(entity)
                                    } else {
                                        // Step 2: Classify cropped sub-bitmap with ML Kit ImageLabeler
                                        val cropInput = InputImage.fromBitmap(cropBitmap, 0)
                                        val labelTask = labeler.process(cropInput)

                                        try {
                                            val labels = Tasks.await(labelTask)
                                            val cropCandidates = labels.map { it.text to it.confidence }
                                            val objCandidates = obj.labels.map { it.text to it.confidence }

                                            val resolved = ObjectKnowledgeBase.resolveBestInfo(cropCandidates, objCandidates)
                                            val info = resolved.info
                                            val conf = resolved.confidence

                                            val adjustedConf = labelMemory?.adjustConfidence(conf, info.id, emptyList()) ?: conf

                                            if (adjustedConf >= minConfidence) {
                                                classificationCache[trackId] = CachedClassification(
                                                    label = info.id,
                                                    displayName = info.name,
                                                    category = info.category,
                                                    confidence = adjustedConf,
                                                    isLearned = false,
                                                    timestamp = now
                                                )

                                                frameDetections.add(
                                                    DetectedObject(
                                                        id = detectionId,
                                                        label = info.id,
                                                        displayName = info.name,
                                                        confidence = adjustedConf,
                                                        category = info.category,
                                                        normalizedLeft = normalizedLeft.coerceIn(0.02f, 0.95f),
                                                        normalizedTop = normalizedTop.coerceIn(0.02f, 0.95f),
                                                        normalizedRight = normalizedRight.coerceIn(0.05f, 0.98f),
                                                        normalizedBottom = normalizedBottom.coerceIn(0.05f, 0.98f),
                                                        trackingId = trackId,
                                                        isLearned = false
                                                    )
                                                )
                                            }
                                        } catch (_: Exception) {}
                                    }
                                }
                            }
                        }
                    }

                    // 2. Primary Center Viewfinder ROI:
                    // If no object is detected near the center reticle, analyze the center region where the user aims
                    val hasCenterObject = frameDetections.any {
                        it.normalizedLeft <= 0.60f && it.normalizedRight >= 0.40f &&
                        it.normalizedTop <= 0.60f && it.normalizedBottom >= 0.40f
                    }

                    if (!hasCenterObject) {
                        val orientedBitmap = getOrientedBitmap()
                        if (orientedBitmap != null && !orientedBitmap.isRecycled) {
                            val roiW = (orientedBitmap.width * 0.50f).toInt().coerceAtLeast(60)
                            val roiH = (orientedBitmap.height * 0.50f).toInt().coerceAtLeast(60)
                            val roiLeft = ((orientedBitmap.width - roiW) / 2).coerceIn(0, orientedBitmap.width - roiW)
                            val roiTop = ((orientedBitmap.height - roiH) / 2).coerceIn(0, orientedBitmap.height - roiH)

                            val centerBitmap = try {
                                Bitmap.createBitmap(orientedBitmap, roiLeft, roiTop, roiW, roiH)
                            } catch (_: Exception) { null }

                            if (centerBitmap != null) {
                                val centerId = "live_center_viewfinder"
                                activeCropMap[centerId] = centerBitmap

                                // Check user-taught custom objects first
                                val userMatch = learnedObjectManager?.findUserTaughtMatch(centerBitmap)
                                if (userMatch != null) {
                                    val entity = userMatch.entity
                                    val conf = userMatch.similarity.coerceIn(0.75f, 0.99f)
                                    frameDetections.add(
                                        DetectedObject(
                                            id = centerId,
                                            label = entity.name.lowercase(),
                                            displayName = entity.name,
                                            confidence = conf,
                                            category = entity.category,
                                            normalizedLeft = 0.22f,
                                            normalizedTop = 0.22f,
                                            normalizedRight = 0.78f,
                                            normalizedBottom = 0.78f,
                                            trackingId = 9999,
                                            isLearned = true
                                        )
                                    )
                                    learnedObjectManager?.recordRecognition(entity)
                                } else {
                                    // Classify Center Viewfinder with ImageLabeler
                                    try {
                                        val centerInput = InputImage.fromBitmap(centerBitmap, 0)
                                        val labels = Tasks.await(labeler.process(centerInput))
                                        val candidates = labels.map { it.text to it.confidence }
                                        val resolved = ObjectKnowledgeBase.resolveBestInfo(candidates, emptyList())
                                        val info = resolved.info
                                        val conf = resolved.confidence

                                        val adjustedConf = labelMemory?.adjustConfidence(conf, info.id, emptyList()) ?: conf

                                        // Only show center ROI detection if it resolved to a specific object (Tier 1)
                                        // or if confidence is high, preventing generic scene labels from showing as center boxes
                                        if (adjustedConf >= minConfidence && resolved.tier == LabelTier.SPECIFIC_OBJECT) {
                                            frameDetections.add(
                                                DetectedObject(
                                                    id = centerId,
                                                    label = info.id,
                                                    displayName = info.name,
                                                    confidence = adjustedConf,
                                                    category = info.category,
                                                    normalizedLeft = 0.22f,
                                                    normalizedTop = 0.22f,
                                                    normalizedRight = 0.78f,
                                                    normalizedBottom = 0.78f,
                                                    trackingId = 9999,
                                                    isLearned = false
                                                )
                                            )
                                        }
                                    } catch (_: Exception) {}
                                }
                            }
                        }
                    }

                    // Apply NMS and temporal smoothing
                    val nms = applyNMS(frameDetections)
                    val crossNms = applyCrossLabelNMS(nms)
                    val smoothed = smoothDetections(crossNms)
                    val finalDetections = applyCrossLabelNMS(smoothed, 0.60f)

                    // Prune active crops for objects no longer in frame
                    val activeIds = finalDetections.map { it.id }.toSet()
                    activeCropMap.keys.retainAll(activeIds)

                    if (finalDetections.isNotEmpty()) {
                        labelMemory?.recordSightings(finalDetections.map { it.label })
                    }

                    // Metrics
                    val latency = System.currentTimeMillis() - frameStartTime
                    _inferenceLatencyMs.value = latency
                    if (lastProcessTime > 0L) {
                        val delta = System.currentTimeMillis() - lastProcessTime
                        if (delta > 0) {
                            _inferenceFps.value = (1000L / delta).toInt().coerceIn(1, 60)
                        }
                    }
                    lastProcessTime = System.currentTimeMillis()

                    onResult(finalDetections)
                } catch (_: Exception) {
                    // Guard against any unexpected frame processing errors
                } finally {
                    imageProxy.close()
                }
            }
        } catch (_: Exception) {
            imageProxy.close()
        }
    }

    /**
     * Non-Maximum Suppression (NMS) to eliminate duplicate overlapping bounding boxes with the same label.
     */
    fun applyNMS(
        detections: List<DetectedObject>,
        iouThreshold: Float = 0.45f
    ): List<DetectedObject> {
        val sorted = detections.sortedByDescending { it.confidence }
        val selected = mutableListOf<DetectedObject>()

        for (candidate in sorted) {
            var shouldSelect = true
            for (chosen in selected) {
                if (candidate.label.equals(chosen.label, ignoreCase = true)) {
                    val iou = calculateIoU(candidate, chosen)
                    if (iou > iouThreshold) {
                        shouldSelect = false
                        break
                    }
                }
            }
            if (shouldSelect) {
                selected.add(candidate)
            }
        }
        return selected
    }

    /**
     * Cross-label NMS: suppresses highly overlapping boxes across different labels.
     */
    private fun applyCrossLabelNMS(
        detections: List<DetectedObject>,
        iouThreshold: Float = 0.70f
    ): List<DetectedObject> {
        val sorted = detections.sortedByDescending { it.confidence }
        val selected = mutableListOf<DetectedObject>()

        for (candidate in sorted) {
            var shouldSelect = true
            for (chosen in selected) {
                val iou = calculateIoU(candidate, chosen)
                if (iou > iouThreshold) {
                    shouldSelect = false
                    break
                }
            }
            if (shouldSelect) {
                selected.add(candidate)
            }
        }
        return selected
    }

    private fun calculateIoU(a: DetectedObject, b: DetectedObject): Float {
        val interLeft = max(a.normalizedLeft, b.normalizedLeft)
        val interTop = max(a.normalizedTop, b.normalizedTop)
        val interRight = min(a.normalizedRight, b.normalizedRight)
        val interBottom = min(a.normalizedBottom, b.normalizedBottom)

        if (interRight <= interLeft || interBottom <= interTop) return 0f

        val interArea = (interRight - interLeft) * (interBottom - interTop)
        val areaA = a.width * a.height
        val areaB = b.width * b.height
        val unionArea = areaA + areaB - interArea

        return if (unionArea > 0f) interArea / unionArea else 0f
    }

    /**
     * Temporal smoothing across frames with a grace period for missed detections.
     */
    fun smoothDetections(
        newDetections: List<DetectedObject>,
        smoothingFactor: Float = 0.35f
    ): List<DetectedObject> {
        val smoothedList = mutableListOf<DetectedObject>()
        val matchedTrackIds = mutableSetOf<Int>()

        for (newDet in newDetections) {
            var matchedTrack: Pair<Int, TrackedObject>? = null
            for ((trackId, tracked) in activeTracks) {
                val prevDet = tracked.detection
                if (prevDet.label.equals(newDet.label, ignoreCase = true) || (prevDet.isLearned && newDet.isLearned)) {
                    val iou = calculateIoU(prevDet, newDet)
                    if (iou > 0.25f) {
                        matchedTrack = trackId to tracked
                        break
                    }
                }
            }

            if (matchedTrack != null) {
                val (trackId, tracked) = matchedTrack
                val prev = tracked.detection
                matchedTrackIds.add(trackId)

                val sLeft = prev.normalizedLeft + (newDet.normalizedLeft - prev.normalizedLeft) * smoothingFactor
                val sTop = prev.normalizedTop + (newDet.normalizedTop - prev.normalizedTop) * smoothingFactor
                val sRight = prev.normalizedRight + (newDet.normalizedRight - prev.normalizedRight) * smoothingFactor
                val sBottom = prev.normalizedBottom + (newDet.normalizedBottom - prev.normalizedBottom) * smoothingFactor
                val sConf = prev.confidence + (newDet.confidence - prev.confidence) * smoothingFactor

                val smoothed = newDet.copy(
                    normalizedLeft = sLeft,
                    normalizedTop = sTop,
                    normalizedRight = sRight,
                    normalizedBottom = sBottom,
                    confidence = sConf,
                    trackingId = trackId
                )
                activeTracks[trackId] = TrackedObject(smoothed, missedFrames = 0)
                smoothedList.add(smoothed)
            } else {
                val newId = nextTrackingId++
                val track = newDet.copy(trackingId = newId)
                activeTracks[newId] = TrackedObject(track, missedFrames = 0)
                smoothedList.add(track)
            }
        }

        // Grace period
        val staleIds = mutableListOf<Int>()
        for ((trackId, tracked) in activeTracks) {
            if (trackId !in matchedTrackIds) {
                val newMissed = tracked.missedFrames + 1
                if (newMissed > TRACK_GRACE_FRAMES) {
                    staleIds.add(trackId)
                } else {
                    val decayed = tracked.detection.copy(
                        confidence = (tracked.detection.confidence * 0.85f).coerceAtLeast(0.01f)
                    )
                    activeTracks[trackId] = TrackedObject(decayed, missedFrames = newMissed)
                    smoothedList.add(decayed)
                }
            }
        }
        staleIds.forEach {
            activeTracks.remove(it)
            classificationCache.remove(it)
        }

        return smoothedList
    }

    /**
     * Analyzes an imported static bitmap using crop-based labeling and learned object matching.
     */
    suspend fun analyzeBitmap(bitmap: Bitmap, minConfidence: Float): List<DetectedObject> =
        withContext(Dispatchers.Default) {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val imageWidth = bitmap.width
            val imageHeight = bitmap.height

            val singleOptions = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
            val singleDetector = ObjectDetection.getClient(singleOptions)

            val labeler = mlkitLabeler
            if (labeler == null) {
                singleDetector.close()
                return@withContext emptyList()
            }

            try {
                val mlkitObjects = Tasks.await(singleDetector.process(inputImage)) ?: emptyList()
                val results = mutableListOf<DetectedObject>()

                for (obj in mlkitObjects) {
                    val box = obj.boundingBox
                    val cropLeft = box.left.coerceIn(0, bitmap.width - 1)
                    val cropTop = box.top.coerceIn(0, bitmap.height - 1)
                    val cropW = box.width().coerceIn(10, bitmap.width - cropLeft)
                    val cropH = box.height().coerceIn(10, bitmap.height - cropTop)

                    val cropBitmap = try {
                        Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropW, cropH)
                    } catch (_: Exception) {
                        null
                    }

                    val detectionId = "bitmap_${nextTrackingId++}"

                    if (cropBitmap != null) {
                        activeCropMap[detectionId] = cropBitmap

                        // 1. Check visual memory (user-taught objects only)
                        val learnedMatch = learnedObjectManager?.findUserTaughtMatch(cropBitmap)
                        if (learnedMatch != null) {
                            results.add(
                                DetectedObject(
                                    id = detectionId,
                                    label = learnedMatch.entity.name.lowercase(),
                                    displayName = learnedMatch.entity.name,
                                    confidence = learnedMatch.similarity,
                                    category = learnedMatch.entity.category,
                                    normalizedLeft = box.left.toFloat() / imageWidth,
                                    normalizedTop = box.top.toFloat() / imageHeight,
                                    normalizedRight = box.right.toFloat() / imageWidth,
                                    normalizedBottom = box.bottom.toFloat() / imageHeight,
                                    isLearned = true
                                )
                            )
                            continue
                        }

                        // 2. Crop labeling + ML Kit detector labels resolved via ObjectKnowledgeBase
                        try {
                            val labels = Tasks.await(labeler.process(InputImage.fromBitmap(cropBitmap, 0)))
                            val cropCandidates = labels.map { it.text to it.confidence }
                            val objCandidates = obj.labels.map { it.text to it.confidence }

                            Log.d("ObjectLens", "STATIC crop labels: ${cropCandidates.joinToString { "${it.first}(${String.format("%.2f", it.second)})" }}")
                            Log.d("ObjectLens", "STATIC obj  labels: ${objCandidates.joinToString { "${it.first}(${String.format("%.2f", it.second)})" }}")

                            val resolved = ObjectKnowledgeBase.resolveBestInfo(cropCandidates, objCandidates)
                            val info = resolved.info
                            val conf = resolved.confidence

                            Log.d("ObjectLens", "STATIC resolved → ${info.name} (${info.id}) conf=${String.format("%.2f", conf)} cat=${info.category}")

                            if (conf >= minConfidence) {
                                results.add(
                                    DetectedObject(
                                        id = detectionId,
                                        label = info.id,
                                        displayName = info.name,
                                        confidence = conf,
                                        category = info.category,
                                        normalizedLeft = box.left.toFloat() / imageWidth,
                                        normalizedTop = box.top.toFloat() / imageHeight,
                                        normalizedRight = box.right.toFloat() / imageWidth,
                                        normalizedBottom = box.bottom.toFloat() / imageHeight,
                                        isLearned = false
                                    )
                                )
                                continue
                            }
                        } catch (_: Exception) {}
                    }

                    // Fallback to detector label
                    val objCandidates = obj.labels.map { it.text to it.confidence }
                    if (objCandidates.isNotEmpty()) {
                        val resolved = ObjectKnowledgeBase.resolveBestInfo(emptyList(), objCandidates)
                        val info = resolved.info
                        val conf = resolved.confidence
                        if (conf >= minConfidence) {
                            results.add(
                                DetectedObject(
                                    id = detectionId,
                                    label = info.id,
                                    displayName = info.name,
                                    confidence = conf,
                                    category = info.category,
                                    normalizedLeft = box.left.toFloat() / imageWidth,
                                    normalizedTop = box.top.toFloat() / imageHeight,
                                    normalizedRight = box.right.toFloat() / imageWidth,
                                    normalizedBottom = box.bottom.toFloat() / imageHeight,
                                    isLearned = false
                                )
                            )
                        }
                    }
                }

                // If ObjectDetector produced no results for this static photo (e.g. close-up or unboxed item):
                if (results.isEmpty()) {
                    try {
                        val fullInput = InputImage.fromBitmap(bitmap, 0)
                        val labels = Tasks.await(labeler.process(fullInput))
                        val candidates = labels.map { it.text to it.confidence }
                        val resolved = ObjectKnowledgeBase.resolveBestInfo(candidates, emptyList())
                        if (resolved.confidence >= minConfidence && resolved.tier == LabelTier.SPECIFIC_OBJECT) {
                            val info = resolved.info
                            val detectionId = "bitmap_full"
                            activeCropMap[detectionId] = bitmap
                            results.add(
                                DetectedObject(
                                    id = detectionId,
                                    label = info.id,
                                    displayName = info.name,
                                    confidence = resolved.confidence,
                                    category = info.category,
                                    normalizedLeft = 0.05f,
                                    normalizedTop = 0.05f,
                                    normalizedRight = 0.95f,
                                    normalizedBottom = 0.95f,
                                    isLearned = false
                                )
                            )
                        }
                    } catch (_: Exception) {}
                }

                if (results.isNotEmpty()) {
                    labelMemory?.recordSightings(results.map { it.label })
                }

                applyNMS(results)
            } catch (e: Exception) {
                emptyList()
            } finally {
                singleDetector.close()
            }
        }

    fun release() {
        try {
            mlkitStreamDetector?.close()
            mlkitLabeler?.close()
            activeCropMap.clear()
            classificationCache.clear()
            backgroundExecutor.shutdown()
        } catch (_: Exception) {}
    }
}
