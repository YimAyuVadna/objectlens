package com.example.ml

enum class ConfidenceLevel {
    HIGH,   // >= 80%
    MEDIUM, // 50% - 79%
    LOW     // < 50%
}

data class DetectedObject(
    val id: String,
    val label: String,
    val displayName: String,
    val confidence: Float,
    val category: String,
    val normalizedLeft: Float,
    val normalizedTop: Float,
    val normalizedRight: Float,
    val normalizedBottom: Float,
    val trackingId: Int = 0,
    val isLearned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    val confidencePercent: Int
        get() = (confidence * 100).coerceIn(0f, 100f).toInt()

    val confidenceLevel: ConfidenceLevel
        get() = when {
            confidence >= 0.80f -> ConfidenceLevel.HIGH
            confidence >= 0.50f -> ConfidenceLevel.MEDIUM
            else -> ConfidenceLevel.LOW
        }

    val width: Float
        get() = (normalizedRight - normalizedLeft).coerceAtLeast(0f)

    val height: Float
        get() = (normalizedBottom - normalizedTop).coerceAtLeast(0f)

    val centerX: Float
        get() = normalizedLeft + width / 2f

    val centerY: Float
        get() = normalizedTop + height / 2f
}
