package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.ObjectLensRepository
import com.example.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val confidenceThreshold: Float = 0.50f,
    val detectionFrequency: String = "Medium",
    val showBoundingBoxes: Boolean = true,
    val showConfidence: Boolean = true,
    val soundFeedback: Boolean = false,
    val hapticFeedback: Boolean = true,
    val defaultCamera: String = "Back",
    val isHistoryCleared: Boolean = false,
    val learnedObjectCount: Int = 0,
    val cloudSyncMessage: String? = null,
    val isCloudSyncing: Boolean = false,
    val exportedJson: String? = null
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PreferencesManager(application)
    private val database = AppDatabase.getInstance(application)
    private val repository = ObjectLensRepository.getInstance(database)
    private val learnedObjectManager = com.example.ml.LearnedObjectManager(
        database.learnedObjectDao(),
        viewModelScope,
        application
    )
    private val cloudSync = com.example.data.sync.CloudLearnedObjectSync(learnedObjectManager)

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            confidenceThreshold = preferences.confidenceThreshold,
            detectionFrequency = preferences.detectionFrequency,
            showBoundingBoxes = preferences.showBoundingBoxes,
            showConfidence = preferences.showConfidence,
            soundFeedback = preferences.soundFeedback,
            hapticFeedback = preferences.hapticFeedback,
            defaultCamera = preferences.defaultCamera
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        refreshLearnedCount()
    }

    fun setConfidenceThreshold(threshold: Float) {
        preferences.confidenceThreshold = threshold
        _uiState.update { it.copy(confidenceThreshold = threshold) }
    }

    fun setDetectionFrequency(freq: String) {
        preferences.detectionFrequency = freq
        _uiState.update { it.copy(detectionFrequency = freq) }
    }

    fun setShowBoundingBoxes(show: Boolean) {
        preferences.showBoundingBoxes = show
        _uiState.update { it.copy(showBoundingBoxes = show) }
    }

    fun setShowConfidence(show: Boolean) {
        preferences.showConfidence = show
        _uiState.update { it.copy(showConfidence = show) }
    }

    fun setSoundFeedback(enabled: Boolean) {
        preferences.soundFeedback = enabled
        _uiState.update { it.copy(soundFeedback = enabled) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        preferences.hapticFeedback = enabled
        _uiState.update { it.copy(hapticFeedback = enabled) }
    }

    fun setDefaultCamera(camera: String) {
        preferences.defaultCamera = camera
        _uiState.update { it.copy(defaultCamera = camera) }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _uiState.update { it.copy(isHistoryCleared = true) }
        }
    }

    fun resetToDefaults() {
        setConfidenceThreshold(0.50f)
        setDetectionFrequency("High")
        setShowBoundingBoxes(true)
        setShowConfidence(true)
        setSoundFeedback(false)
        setHapticFeedback(true)
        setDefaultCamera("Back")
    }

    fun dismissHistoryClearedNotification() {
        _uiState.update { it.copy(isHistoryCleared = false) }
    }

    fun refreshLearnedCount() {
        viewModelScope.launch {
            learnedObjectManager.loadFromDatabase()
            _uiState.update { it.copy(learnedObjectCount = learnedObjectManager.getObjectCount()) }
        }
    }

    fun syncWithCloud() {
        _uiState.update { it.copy(isCloudSyncing = true, cloudSyncMessage = null) }
        viewModelScope.launch {
            learnedObjectManager.loadFromDatabase()
            cloudSync.syncWithCloud { success, count, message ->
                _uiState.update {
                    it.copy(
                        isCloudSyncing = false,
                        learnedObjectCount = learnedObjectManager.getObjectCount(),
                        cloudSyncMessage = message
                    )
                }
            }
        }
    }

    fun exportLearnedObjects(onExported: (String) -> Unit) {
        viewModelScope.launch {
            val json = cloudSync.exportToJson()
            _uiState.update {
                it.copy(
                    exportedJson = json,
                    cloudSyncMessage = "Exported ${learnedObjectManager.getObjectCount()} objects to JSON"
                )
            }
            onExported(json)
        }
    }

    fun importLearnedObjects(jsonStr: String) {
        viewModelScope.launch {
            val count = cloudSync.importFromJson(jsonStr)
            _uiState.update {
                it.copy(
                    learnedObjectCount = learnedObjectManager.getObjectCount(),
                    cloudSyncMessage = "Successfully merged $count learned object(s) into memory!"
                )
            }
        }
    }

    fun dismissCloudSyncMessage() {
        _uiState.update { it.copy(cloudSyncMessage = null) }
    }
}
