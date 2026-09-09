package com.example.ml

import android.content.Context
import android.graphics.Bitmap
import com.example.data.local.LearnedObjectDao
import com.example.data.local.LearnedObjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class LearnedMatch(
    val entity: LearnedObjectEntity,
    val similarity: Float
)

class LearnedObjectManager(
    private val dao: LearnedObjectDao,
    private val scope: CoroutineScope,
    private val context: Context? = null
) {
    // In-memory cache of learned objects: id -> (entity, list of exemplar vectors)
    private val learnedCache = ConcurrentHashMap<String, Pair<LearnedObjectEntity, MutableList<FloatArray>>>()
    private var isLoaded = false
    private val lastDbUpdateTime = ConcurrentHashMap<String, Long>()

    companion object {
        private const val MAX_EXEMPLARS_PER_OBJECT = 5
        private const val DB_RECOGNITION_DEBOUNCE_MS = 5000L
        private const val DEFAULT_MATCH_THRESHOLD = 0.60f
    }

    suspend fun loadFromDatabase(ctx: Context? = null) {
        val activeContext = ctx ?: context
        val currentCount = dao.getCount()
        if (currentCount < 2000) {
            val defaults = com.example.data.local.PrepopulatedLearnedObjects.getAll(activeContext)
            val existing = dao.getAllLearnedObjects()
            val existingIds = existing.map { it.id }.toSet()
            val toInsert = defaults.filter { !existingIds.contains(it.id) }
            if (toInsert.isNotEmpty()) {
                toInsert.chunked(100).forEach { chunk ->
                    dao.insertAll(chunk)
                }
            }
        }

        val entities = dao.getAllLearnedObjects()
        learnedCache.clear()
        for (entity in entities) {
            val vectors = VisualFeatureExtractor.deserializeVectors(entity.featureVector)
            if (vectors.isNotEmpty()) {
                learnedCache[entity.id] = entity to vectors.toMutableList()
            }
        }
        isLoaded = true
    }

    /**
     * Checks if the given crop matches any custom user-taught object.
     * Evaluates against all exemplar view angles of each object.
     * User-taught objects require threshold >= [threshold] (calibrated 0.70f).
     */
    fun findUserTaughtMatch(cropBitmap: Bitmap, threshold: Float = 0.70f): LearnedMatch? {
        if (!isLoaded || learnedCache.isEmpty()) return null

        val queryVector = VisualFeatureExtractor.extractFeatureVector(cropBitmap)
        var bestMatch: LearnedMatch? = null
        var maxSim = threshold

        for ((_, pair) in learnedCache) {
            val (entity, exemplars) = pair
            // ONLY match custom objects taught by a user (never pre_* synthetic items)
            if (entity.id.startsWith("pre_")) continue

            for (exemplar in exemplars) {
                val sim = VisualFeatureExtractor.cosineSimilarity(queryVector, exemplar)
                if (sim > maxSim) {
                    maxSim = sim
                    bestMatch = LearnedMatch(entity, sim)
                }
            }
        }
        return bestMatch
    }

    /**
     * Checks if the given crop matches any learned object.
     * Evaluates user-taught objects.
     */
    fun findMatch(cropBitmap: Bitmap, threshold: Float = DEFAULT_MATCH_THRESHOLD): LearnedMatch? {
        return findUserTaughtMatch(cropBitmap, threshold)
    }

    /**
     * Teaches the camera an object.
     * If an object with the same name already exists, enriches its visual memory
     * with an additional view angle/exemplar instead of creating conflicting duplicates.
     * Previous objects are strictly preserved and never overwritten or forgotten.
     */
    fun teachObject(
        cropBitmap: Bitmap,
        name: String,
        category: String = "Custom"
    ): LearnedObjectEntity {
        val newVector = VisualFeatureExtractor.extractFeatureVector(cropBitmap)
        val cleanName = name.trim()
        val cleanCategory = category.trim().ifBlank { "Learned" }

        // Check if an object with this name already exists in memory
        val existingEntry = learnedCache.values.firstOrNull {
            it.first.name.equals(cleanName, ignoreCase = true)
        }

        if (existingEntry != null) {
            val (existingEntity, exemplars) = existingEntry
            val closestIndex = exemplars.indices.maxByOrNull {
                VisualFeatureExtractor.cosineSimilarity(newVector, exemplars[it])
            }

            if (closestIndex != null && VisualFeatureExtractor.cosineSimilarity(newVector, exemplars[closestIndex]) > 0.85f) {
                // Similar angle/lighting: refine the closest exemplar
                exemplars[closestIndex] = VisualFeatureExtractor.blendVectors(
                    exemplars[closestIndex],
                    newVector,
                    existingEntity.sampleCount
                )
            } else if (exemplars.size < MAX_EXEMPLARS_PER_OBJECT) {
                // New distinct view angle: add as a new exemplar
                exemplars.add(newVector)
            } else {
                // Buffer full: replace the most similar exemplar to preserve maximum variety
                val replaceIdx = closestIndex ?: 0
                exemplars[replaceIdx] = newVector
            }

            val updatedEntity = existingEntity.copy(
                category = cleanCategory,
                featureVector = VisualFeatureExtractor.serializeVectors(exemplars),
                sampleCount = existingEntity.sampleCount + 1,
                lastRecognizedAt = System.currentTimeMillis()
            )

            learnedCache[existingEntity.id] = updatedEntity to exemplars
            scope.launch(Dispatchers.IO) {
                dao.update(updatedEntity)
            }
            return updatedEntity
        } else {
            // New distinct object: add without affecting existing objects
            val exemplars = mutableListOf(newVector)
            val entity = LearnedObjectEntity(
                id = UUID.randomUUID().toString(),
                name = cleanName,
                category = cleanCategory,
                featureVector = VisualFeatureExtractor.serializeVectors(exemplars),
                sampleCount = 1,
                createdAt = System.currentTimeMillis(),
                lastRecognizedAt = System.currentTimeMillis()
            )

            learnedCache[entity.id] = entity to exemplars
            scope.launch(Dispatchers.IO) {
                dao.insert(entity)
            }
            return entity
        }
    }

    /**
     * Records a passive match during live camera scanning.
     * Updates timestamp without corrupting reference feature vectors with live background noise.
     */
    fun recordRecognition(entity: LearnedObjectEntity) {
        val now = System.currentTimeMillis()
        val lastUpdate = lastDbUpdateTime[entity.id] ?: 0L
        if (now - lastUpdate > DB_RECOGNITION_DEBOUNCE_MS) {
            lastDbUpdateTime[entity.id] = now
            val pair = learnedCache[entity.id]
            if (pair != null) {
                val updated = pair.first.copy(lastRecognizedAt = now)
                learnedCache[entity.id] = updated to pair.second
                scope.launch(Dispatchers.IO) {
                    dao.update(updated)
                }
            }
        }
    }

    /**
     * Merges a batch of learned objects (e.g. from Cloud Sync or JSON Import).
     */
    suspend fun mergeObjects(incoming: List<LearnedObjectEntity>): Int {
        var mergedCount = 0
        for (item in incoming) {
            val incomingVectors = VisualFeatureExtractor.deserializeVectors(item.featureVector)
            if (incomingVectors.isEmpty()) continue

            val existingEntry = learnedCache.values.firstOrNull {
                it.first.name.equals(item.name.trim(), ignoreCase = true)
            }

            if (existingEntry != null) {
                val (existing, currentExemplars) = existingEntry
                var addedAny = false
                for (inVec in incomingVectors) {
                    val maxSim = currentExemplars.maxOfOrNull {
                        VisualFeatureExtractor.cosineSimilarity(inVec, it)
                    } ?: 0f
                    if (maxSim < 0.85f && currentExemplars.size < MAX_EXEMPLARS_PER_OBJECT) {
                        currentExemplars.add(inVec)
                        addedAny = true
                    }
                }
                if (addedAny) {
                    val updated = existing.copy(
                        featureVector = VisualFeatureExtractor.serializeVectors(currentExemplars),
                        sampleCount = existing.sampleCount + incomingVectors.size,
                        lastRecognizedAt = maxOf(existing.lastRecognizedAt, item.lastRecognizedAt)
                    )
                    learnedCache[existing.id] = updated to currentExemplars
                    dao.update(updated)
                    mergedCount++
                }
            } else {
                learnedCache[item.id] = item to incomingVectors.toMutableList()
                dao.insert(item)
                mergedCount++
            }
        }
        return mergedCount
    }

    fun getAllLearned(): List<LearnedObjectEntity> {
        return learnedCache.values.map { it.first }
    }

    fun getObjectCount(): Int {
        return learnedCache.size
    }
}
