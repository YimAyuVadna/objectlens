package com.example.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("objectlens_preferences", Context.MODE_PRIVATE)

    var confidenceThreshold: Float
        get() = prefs.getFloat(KEY_CONFIDENCE_THRESHOLD, 0.50f)
        set(value) = prefs.edit().putFloat(KEY_CONFIDENCE_THRESHOLD, value).apply()

    var detectionFrequency: String
        get() = prefs.getString(KEY_DETECTION_FREQUENCY, "High") ?: "High"
        set(value) = prefs.edit().putString(KEY_DETECTION_FREQUENCY, value).apply()

    var showBoundingBoxes: Boolean
        get() = prefs.getBoolean(KEY_SHOW_BOUNDING_BOXES, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_BOUNDING_BOXES, value).apply()

    var showConfidence: Boolean
        get() = prefs.getBoolean(KEY_SHOW_CONFIDENCE, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_CONFIDENCE, value).apply()

    var soundFeedback: Boolean
        get() = prefs.getBoolean(KEY_SOUND_FEEDBACK, false)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_FEEDBACK, value).apply()

    var hapticFeedback: Boolean
        get() = prefs.getBoolean(KEY_HAPTIC_FEEDBACK, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTIC_FEEDBACK, value).apply()

    var defaultCamera: String
        get() = prefs.getString(KEY_DEFAULT_CAMERA, "Back") ?: "Back"
        set(value) = prefs.edit().putString(KEY_DEFAULT_CAMERA, value).apply()

    var hasCompletedOnboarding: Boolean
        get() = prefs.getBoolean(KEY_HAS_COMPLETED_ONBOARDING, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_COMPLETED_ONBOARDING, value).apply()

    companion object {
        private const val KEY_CONFIDENCE_THRESHOLD = "confidence_threshold"
        private const val KEY_DETECTION_FREQUENCY = "detection_frequency"
        private const val KEY_SHOW_BOUNDING_BOXES = "show_bounding_boxes"
        private const val KEY_SHOW_CONFIDENCE = "show_confidence"
        private const val KEY_SOUND_FEEDBACK = "sound_feedback"
        private const val KEY_HAPTIC_FEEDBACK = "haptic_feedback"
        private const val KEY_DEFAULT_CAMERA = "default_camera"
        private const val KEY_HAS_COMPLETED_ONBOARDING = "has_completed_onboarding"
    }
}
