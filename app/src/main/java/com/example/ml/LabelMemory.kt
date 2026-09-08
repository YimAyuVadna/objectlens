package com.example.ml

import com.example.data.local.LearnedLabelEntity
import com.example.data.local.ScanDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.math.ln

class LabelMemory(private val dao: ScanDao, private val scope: CoroutineScope) {

    // In-memory caches (loaded from Room on init)
    private val sightings = mutableMapOf<String, Int>()       // label -> total sighting count
    private val confirmations = mutableMapOf<String, Int>()    // label -> user confirmation count  
    private val coOccurrenceMap = mutableMapOf<String, MutableMap<String, Int>>() // label -> (other_label -> count)
    private var isLoaded = false

    // Initialize: load all persisted learned labels from Room into memory
    suspend fun loadFromDatabase() {
        val entities = dao.getAllLearnedLabels()
        for (entity in entities) {
            sightings[entity.label] = entity.sightingCount
            confirmations[entity.label] = entity.confirmationCount
            // Parse co-occurrences from JSON string
            if (entity.coOccurrences.isNotBlank()) {
                try {
                    val json = JSONObject(entity.coOccurrences)
                    val map = mutableMapOf<String, Int>()
                    json.keys().forEach { key -> map[key] = json.getInt(key) }
                    coOccurrenceMap[entity.label] = map
                } catch (_: Exception) {}
            }
        }
        isLoaded = true
    }

    // Record that these labels were all seen together in one frame.
    // Called on every detection frame.
    // Debounce: only record sightings at most once per second per label to avoid flooding.
    private val lastSightingTime = mutableMapOf<String, Long>()

    fun recordSightings(labels: List<String>) {
        val now = System.currentTimeMillis()
        val uniqueLabels = labels.map { it.lowercase() }.distinct()

        for (label in uniqueLabels) {
            val lastTime = lastSightingTime[label] ?: 0L
            if (now - lastTime < 1000L) continue  // debounce: 1 second
            lastSightingTime[label] = now

            sightings[label] = (sightings[label] ?: 0) + 1

            // Update co-occurrences with other labels in this frame
            for (other in uniqueLabels) {
                if (other == label) continue
                val coMap = coOccurrenceMap.getOrPut(label) { mutableMapOf() }
                coMap[other] = (coMap[other] ?: 0) + 1
            }
        }

        // Persist asynchronously (batched, don't block detection)
        scope.launch(Dispatchers.IO) {
            for (label in uniqueLabels) {
                persistLabel(label)
            }
        }
    }

    // User confirmed a label (tapped on it, saved a scan with it).
    fun confirmLabel(label: String) {
        val key = label.lowercase()
        confirmations[key] = (confirmations[key] ?: 0) + 1
        scope.launch(Dispatchers.IO) {
            persistLabel(key)
        }
    }

    // Confirm all labels at once (e.g., when saving a scan).
    fun confirmLabels(labels: List<String>) {
        for (label in labels) {
            confirmLabel(label)
        }
    }

    // Calculate a confidence boost factor for a given label.
    // Returns a multiplier >= 1.0 (no penalty, only boost).
    // Max boost is 1.20 (20% increase).
    fun getConfidenceMultiplier(label: String): Float {
        if (!isLoaded) return 1.0f
        val key = label.lowercase()

        val sightCount = sightings[key] ?: 0
        val confirmCount = confirmations[key] ?: 0

        // Frequency boost: logarithmic scaling, caps at +10%
        // After ~50 sightings, gets full frequency boost
        val frequencyBoost = if (sightCount > 0) {
            (ln(sightCount.toDouble() + 1.0) / ln(51.0)).coerceAtMost(1.0).toFloat() * 0.10f
        } else 0f

        // Confirmation boost: each confirmation adds up to +10% total
        // After ~10 confirmations, gets full confirmation boost
        val confirmBoost = if (confirmCount > 0) {
            (confirmCount.toFloat() / 10f).coerceAtMost(1.0f) * 0.10f
        } else 0f

        return 1.0f + frequencyBoost + confirmBoost
    }

    // Get a co-occurrence boost for a label given other labels detected in the same frame.
    // If "keyboard" and "mouse" frequently co-occur, detecting one boosts the other.
    // Returns a multiplier >= 1.0, max 1.10 (10% boost).
    fun getCoOccurrenceBoost(label: String, otherLabelsInFrame: List<String>): Float {
        if (!isLoaded) return 1.0f
        val key = label.lowercase()
        val coMap = coOccurrenceMap[key] ?: return 1.0f

        var totalCoCount = 0
        for (other in otherLabelsInFrame) {
            val otherKey = other.lowercase()
            if (otherKey == key) continue
            totalCoCount += coMap[otherKey] ?: 0
        }

        if (totalCoCount == 0) return 1.0f

        // Logarithmic scaling: after ~20 co-occurrences, gets full boost
        val boost = (ln(totalCoCount.toDouble() + 1.0) / ln(21.0)).coerceAtMost(1.0).toFloat() * 0.10f
        return 1.0f + boost
    }

    // Combine all boosts for a final adjusted confidence.
    // Total boost is capped at 1.25 (25% max increase).
    fun adjustConfidence(rawConfidence: Float, label: String, otherLabelsInFrame: List<String>): Float {
        val multiplier = getConfidenceMultiplier(label)
        val coBoost = getCoOccurrenceBoost(label, otherLabelsInFrame)
        val totalMultiplier = (multiplier * coBoost).coerceAtMost(1.25f)
        return (rawConfidence * totalMultiplier).coerceAtMost(0.99f)
    }

    private suspend fun persistLabel(label: String) {
        val coMap = coOccurrenceMap[label]
        val coJson = if (coMap != null && coMap.isNotEmpty()) {
            val json = JSONObject()
            coMap.forEach { (k, v) -> json.put(k, v) }
            json.toString()
        } else ""

        dao.upsertLearnedLabel(
            LearnedLabelEntity(
                label = label,
                sightingCount = sightings[label] ?: 0,
                confirmationCount = confirmations[label] ?: 0,
                lastSeen = System.currentTimeMillis(),
                coOccurrences = coJson
            )
        )
    }
}
