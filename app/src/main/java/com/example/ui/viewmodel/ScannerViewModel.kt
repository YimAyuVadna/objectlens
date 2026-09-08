package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ObjectInformation
import com.example.data.local.ObjectKnowledgeBase
import com.example.data.repository.ObjectLensRepository
import com.example.ml.DetectedObject
import com.example.ml.LabelMemory
import com.example.ml.LearnedObjectManager
import com.example.ml.ObjectDetectorEngine
import com.example.util.HapticManager
import com.example.util.PreferencesManager
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScannerUiState(
    val hasCameraPermission: Boolean = false,
    val isDetectionPaused: Boolean = false,
    val isTorchOn: Boolean = false,
    val useFrontCamera: Boolean = false,
    val detections: List<DetectedObject> = emptyList(),
    val selectedObject: ObjectInformation? = null,
    val selectedDetectedObject: DetectedObject? = null,
    val selectedConfidencePercent: Int? = null,
    val isSelectedObjectFavorite: Boolean = false,
    val showResultDialog: Boolean = false,
    val capturedDetections: List<DetectedObject> = emptyList(),
    val isCapturedSaved: Boolean = false,
    val showTeachDialog: Boolean = false,
    val objectToTeach: DetectedObject? = null,
    val teachCropBitmap: Bitmap? = null,
    val fps: Int = 0,
    val latencyMs: Long = 0L,
    val confidenceThreshold: Float = 0.50f,
    val showBoundingBoxes: Boolean = true,
    val showConfidence: Boolean = true
)

class ScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ObjectLensRepository.getInstance(database)
    private val detectorEngine = ObjectDetectorEngine()
    private val labelMemory = LabelMemory(
        dao = database.scanDao(),
        scope = viewModelScope
    )
    private val learnedObjectManager = LearnedObjectManager(
        dao = database.learnedObjectDao(),
        scope = viewModelScope,
        context = application
    )
    private val cloudSync = com.example.data.sync.CloudLearnedObjectSync(
        learnedObjectManager = learnedObjectManager,
        scope = viewModelScope
    )
    private val preferences = PreferencesManager(application)
    private val ttsManager = TtsManager(application)
    private val hapticManager = HapticManager(application)

    private val _uiState = MutableStateFlow(
        ScannerUiState(
            confidenceThreshold = preferences.confidenceThreshold,
            showBoundingBoxes = preferences.showBoundingBoxes,
            showConfidence = preferences.showConfidence
        )
    )
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    init {
        val targetFps = preferences.detectionFrequency
        detectorEngine.targetFpsMode = targetFps
        val defaultFront = preferences.defaultCamera.equals("Front", ignoreCase = true)
        _uiState.update { it.copy(useFrontCamera = defaultFront) }

        viewModelScope.launch {
            labelMemory.loadFromDatabase()
            learnedObjectManager.loadFromDatabase()
            detectorEngine.labelMemory = labelMemory
            detectorEngine.learnedObjectManager = learnedObjectManager
            detectorEngine.initialize()

            // Silently sync with global cloud pool so objects taught by other users are loaded
            cloudSync.syncWithCloud { _, _, _ -> }
        }
    }

    /**
     * Called by the CameraPreviewView analyzer for every live camera frame.
     * The [imageProxy] is closed automatically by the engine after processing.
     */
    fun onCameraFrame(imageProxy: ImageProxy) {
        if (_uiState.value.isDetectionPaused) {
            imageProxy.close()
            return
        }

        detectorEngine.processLiveFrame(
            imageProxy = imageProxy,
            minConfidence = _uiState.value.confidenceThreshold,
            isFrontCamera = _uiState.value.useFrontCamera
        ) { detections ->
            _uiState.update {
                it.copy(
                    detections = detections,
                    fps = detectorEngine.inferenceFps.value,
                    latencyMs = detectorEngine.inferenceLatencyMs.value
                )
            }

            // Audio and Haptic announcements for new high-confidence detections
            announceDetections(detections)
        }
    }

    /**
     * Trigger TTS and haptic feedback for high-confidence detections.
     */
    private fun announceDetections(detections: List<DetectedObject>) {
        if (detections.isNotEmpty()) {
            val highest = detections.maxByOrNull { it.confidence }
            if (highest != null && highest.confidence >= 0.70f) {
                if (preferences.soundFeedback) {
                    ttsManager.speakObject(highest.displayName)
                }
                if (preferences.hapticFeedback) {
                    hapticManager.performDetectionTick()
                }
            }
        }
    }

    fun setCameraPermission(granted: Boolean) {
        _uiState.update { it.copy(hasCameraPermission = granted) }
    }

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun flipCamera() {
        _uiState.update { it.copy(useFrontCamera = !it.useFrontCamera) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isDetectionPaused = !it.isDetectionPaused) }
    }

    fun captureScan() {
        val currentDetections = _uiState.value.detections
        _uiState.update {
            it.copy(
                showResultDialog = true,
                capturedDetections = currentDetections,
                isCapturedSaved = false
            )
        }
    }

    fun saveCapturedScan() {
        val captured = _uiState.value.capturedDetections
        if (captured.isEmpty()) return

        // Confirm all detected labels in the learning system
        labelMemory.confirmLabels(captured.map { it.label })

        val primary = captured.maxByOrNull { it.confidence } ?: captured.first()
        viewModelScope.launch {
            repository.saveScan(
                primaryObject = primary.displayName,
                primaryCategory = primary.category,
                detections = captured,
                sceneType = "CAMERA"
            )
            _uiState.update { it.copy(isCapturedSaved = true) }
        }
    }

    fun dismissResultDialog() {
        _uiState.update { it.copy(showResultDialog = false) }
    }

    fun selectObjectForDetails(obj: DetectedObject) {
        val info = ObjectKnowledgeBase.getObjectInfo(obj.label)
        // Reinforce this label in the learning system
        labelMemory.confirmLabel(obj.label)
        viewModelScope.launch {
            repository.isFavorite(info.id).collect { isFav ->
                _uiState.update {
                    it.copy(
                        selectedObject = info,
                        selectedDetectedObject = obj,
                        selectedConfidencePercent = obj.confidencePercent,
                        isSelectedObjectFavorite = isFav
                    )
                }
            }
        }
    }

    fun selectObjectByName(name: String) {
        val info = ObjectKnowledgeBase.getObjectInfo(name)
        viewModelScope.launch {
            repository.isFavorite(info.id).collect { isFav ->
                _uiState.update {
                    it.copy(
                        selectedObject = info,
                        selectedDetectedObject = null,
                        selectedConfidencePercent = null,
                        isSelectedObjectFavorite = isFav
                    )
                }
            }
        }
    }

    fun dismissObjectDetails() {
        _uiState.update {
            it.copy(
                selectedObject = null,
                selectedDetectedObject = null,
                selectedConfidencePercent = null
            )
        }
    }

    // --- On-Device Object Teaching Methods ---

    fun openTeachDialog(obj: DetectedObject) {
        val crop = detectorEngine.getCropForObject(obj)
        _uiState.update {
            it.copy(
                showTeachDialog = true,
                objectToTeach = obj,
                teachCropBitmap = crop
            )
        }
    }

    fun dismissTeachDialog() {
        _uiState.update {
            it.copy(
                showTeachDialog = false,
                objectToTeach = null,
                teachCropBitmap = null
            )
        }
    }

    fun teachObject(name: String, category: String) {
        val crop = _uiState.value.teachCropBitmap
        if (crop != null && name.isNotBlank()) {
            val entity = learnedObjectManager.teachObject(crop, name, category)
            labelMemory.confirmLabel(name)
            detectorEngine.clearClassificationCache()
            cloudSync.publishObjectToCloud(entity)
        }
        dismissTeachDialog()
    }

    fun toggleFavorite(info: ObjectInformation) {
        viewModelScope.launch {
            val current = _uiState.value.isSelectedObjectFavorite
            repository.toggleFavorite(info, current)
            _uiState.update { it.copy(isSelectedObjectFavorite = !current) }
        }
    }

    fun processImportedBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            val results = detectorEngine.analyzeBitmap(bitmap, _uiState.value.confidenceThreshold)
            _uiState.update {
                it.copy(
                    showResultDialog = true,
                    capturedDetections = results,
                    isCapturedSaved = false
                )
            }
        }
    }

    fun updatePreferences() {
        val targetFps = preferences.detectionFrequency
        detectorEngine.targetFpsMode = targetFps
        _uiState.update {
            it.copy(
                confidenceThreshold = preferences.confidenceThreshold,
                showBoundingBoxes = preferences.showBoundingBoxes,
                showConfidence = preferences.showConfidence,
                useFrontCamera = preferences.defaultCamera.equals("Front", ignoreCase = true)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        detectorEngine.release()
        ttsManager.shutdown()
    }
}
