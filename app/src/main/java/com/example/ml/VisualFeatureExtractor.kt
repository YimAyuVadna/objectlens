package com.example.ml

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.sqrt

object VisualFeatureExtractor {

    private const val SAMPLE_SIZE = 32
    private const val VECTOR_DIM = 48

    /**
     * Extracts a 48-dimensional zero-mean, L2-normalized visual fingerprint from a bitmap crop.
     * Features include foreground center core, context background margin, and saturation-weighted hue.
     * Zero-mean centering eliminates positive-orthant bias, preventing false cross-object collisions.
     */
    fun extractFeatureVector(bitmap: Bitmap): FloatArray {
        if (bitmap.isRecycled || bitmap.width <= 0 || bitmap.height <= 0) {
            return FloatArray(VECTOR_DIM)
        }

        val resized = if (bitmap.width != SAMPLE_SIZE || bitmap.height != SAMPLE_SIZE) {
            try {
                Bitmap.createScaledBitmap(bitmap, SAMPLE_SIZE, SAMPLE_SIZE, true)
            } catch (_: Exception) {
                return FloatArray(VECTOR_DIM)
            }
        } else {
            bitmap
        }

        val features = FloatArray(VECTOR_DIM)
        val pixels = IntArray(SAMPLE_SIZE * SAMPLE_SIZE)
        resized.getPixels(pixels, 0, SAMPLE_SIZE, 0, 0, SAMPLE_SIZE, SAMPLE_SIZE)

        // 1. Multi-Region Spatial Analysis (4 regions x 8 features = 32 features)
        // Region 0: Top Half (y: 0..15)
        // Region 1: Bottom Half (y: 16..31)
        // Region 2: Center Core (x: 8..23, y: 8..23) - Core foreground object!
        // Region 3: Outer Margin (border ring) - Background desk/room context!
        val hsv = FloatArray(3)

        for (rIdx in 0 until 4) {
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            var hSum = 0f
            var sSum = 0f
            var vSum = 0f
            var gradXSum = 0f
            var gradYSum = 0f
            var pixelCount = 0

            for (y in 0 until SAMPLE_SIZE) {
                for (x in 0 until SAMPLE_SIZE) {
                    val inRegion = when (rIdx) {
                        0 -> y < 16
                        1 -> y >= 16
                        2 -> x in 8..23 && y in 8..23
                        3 -> x < 6 || x >= 26 || y < 6 || y >= 26
                        else -> false
                    }

                    if (!inRegion) continue
                    pixelCount++

                    val px = pixels[y * SAMPLE_SIZE + x]
                    val r = Color.red(px) / 255f
                    val g = Color.green(px) / 255f
                    val b = Color.blue(px) / 255f

                    Color.colorToHSV(px, hsv)
                    rSum += r
                    gSum += g
                    bSum += b
                    hSum += hsv[0] / 360f
                    sSum += hsv[1]
                    vSum += hsv[2]

                    if (x < SAMPLE_SIZE - 1) {
                        val pxRight = pixels[y * SAMPLE_SIZE + (x + 1)]
                        gradXSum += kotlin.math.abs(r - Color.red(pxRight) / 255f)
                    }
                    if (y < SAMPLE_SIZE - 1) {
                        val pxDown = pixels[(y + 1) * SAMPLE_SIZE + x]
                        gradYSum += kotlin.math.abs(r - Color.red(pxDown) / 255f)
                    }
                }
            }

            val count = pixelCount.coerceAtLeast(1).toFloat()
            val baseIdx = rIdx * 8
            features[baseIdx + 0] = rSum / count
            features[baseIdx + 1] = gSum / count
            features[baseIdx + 2] = bSum / count
            features[baseIdx + 3] = hSum / count
            features[baseIdx + 4] = sSum / count
            features[baseIdx + 5] = vSum / count
            features[baseIdx + 6] = gradXSum / count
            features[baseIdx + 7] = gradYSum / count
        }

        // 2. Global Color Histogram (16 features)
        val hist = FloatArray(16)
        for (px in pixels) {
            Color.colorToHSV(px, hsv)
            val hueBin = ((hsv[0] / 360f) * 15.99f).toInt().coerceIn(0, 15)
            hist[hueBin] += hsv[1] * hsv[2] // Weight by saturation and value
        }
        val histSum = hist.sum().coerceAtLeast(0.0001f)
        for (i in 0 until 16) {
            features[32 + i] = hist[i] / histSum
        }

        if (resized !== bitmap) {
            resized.recycle()
        }

        // Zero-Mean Centering & L2 Normalization
        return normalizeVector(features)
    }

    /**
     * Centers a vector around zero (mean = 0) and scales to unit length (L2 norm = 1.0).
     * This ensures cosine similarity behaves as Pearson correlation:
     * - Unrelated objects score ~ 0.0 or negative.
     * - Matching objects score > 0.60.
     */
    fun normalizeVector(vec: FloatArray): FloatArray {
        if (vec.size != VECTOR_DIM) return FloatArray(VECTOR_DIM)

        var sum = 0f
        for (f in vec) sum += f
        val mean = sum / vec.size

        val centered = FloatArray(VECTOR_DIM) { vec[it] - mean }

        var normSq = 0f
        for (f in centered) normSq += f * f
        val norm = sqrt(normSq).coerceAtLeast(0.00001f)
        for (i in centered.indices) centered[i] /= norm

        return centered
    }

    /**
     * Computes cosine similarity between two zero-mean, L2-normalized feature vectors.
     * Returns a score between -1.0 and 1.0.
     */
    fun cosineSimilarity(v1: FloatArray, v2: FloatArray): Float {
        if (v1.size != v2.size || v1.isEmpty()) return 0f
        var dot = 0f
        for (i in v1.indices) {
            dot += v1[i] * v2[i]
        }
        return dot.coerceIn(-1f, 1f)
    }

    /**
     * Blends a new feature vector into an existing running vector.
     */
    fun blendVectors(current: FloatArray, newSample: FloatArray, sampleCount: Int): FloatArray {
        val blended = FloatArray(VECTOR_DIM)
        val weight = 1f / (sampleCount + 1).coerceAtMost(5).toFloat()
        for (i in 0 until VECTOR_DIM) {
            blended[i] = current[i] * (1f - weight) + newSample[i] * weight
        }
        return normalizeVector(blended)
    }

    fun serializeVector(vector: FloatArray): String {
        return vector.joinToString(",") { java.lang.String.format(java.util.Locale.US, "%.5f", it) }
    }

    fun deserializeVector(str: String): FloatArray? {
        return try {
            val parts = str.split(",")
            if (parts.size != VECTOR_DIM) null
            else {
                val raw = FloatArray(VECTOR_DIM) { parts[it].toFloat() }
                normalizeVector(raw)
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Serializes multiple exemplar vectors into a semicolon-separated string.
     */
    fun serializeVectors(vectors: List<FloatArray>): String {
        return vectors.joinToString(";") { serializeVector(it) }
    }

    /**
     * Deserializes multiple exemplar vectors from a string.
     * Backward-compatible with single-vector comma-separated records.
     */
    fun deserializeVectors(str: String): List<FloatArray> {
        if (str.isBlank()) return emptyList()
        val segments = str.split(";")
        val result = mutableListOf<FloatArray>()
        for (seg in segments) {
            val trimmed = seg.trim()
            if (trimmed.isNotEmpty()) {
                val vec = deserializeVector(trimmed)
                if (vec != null) {
                    result.add(vec)
                }
            }
        }
        return result
    }
}
