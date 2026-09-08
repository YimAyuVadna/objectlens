package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ObjectInformation
import com.example.data.local.ObjectKnowledgeBase
import com.example.data.local.ScanDetectionEntity
import com.example.data.local.ScanRecordEntity
import com.example.data.repository.ObjectLensRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedScan: ScanRecordEntity? = null,
    val selectedScanDetections: List<ScanDetectionEntity> = emptyList(),
    val inspectObject: ObjectInformation? = null,
    val isInspectFavorite: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ObjectLensRepository.getInstance(AppDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    val scans: StateFlow<List<ScanRecordEntity>> = _uiState
        .flatMapLatest { state ->
            if (state.searchQuery.isBlank()) {
                repository.allScans
            } else {
                repository.searchScans(state.searchQuery)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favorites = repository.allFavorites.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun selectScan(scan: ScanRecordEntity) {
        viewModelScope.launch {
            val detections = repository.getDetectionsForScan(scan.id)
            _uiState.update {
                it.copy(
                    selectedScan = scan,
                    selectedScanDetections = detections
                )
            }
        }
    }

    fun dismissScanDetail() {
        _uiState.update {
            it.copy(
                selectedScan = null,
                selectedScanDetections = emptyList()
            )
        }
    }

    fun deleteScan(scanId: Long) {
        viewModelScope.launch {
            repository.deleteScan(scanId)
            if (_uiState.value.selectedScan?.id == scanId) {
                dismissScanDetail()
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            dismissScanDetail()
        }
    }

    fun inspectObjectByName(name: String) {
        val info = ObjectKnowledgeBase.getObjectInfo(name)
        viewModelScope.launch {
            repository.isFavorite(info.id).collect { isFav ->
                _uiState.update {
                    it.copy(
                        inspectObject = info,
                        isInspectFavorite = isFav
                    )
                }
            }
        }
    }

    fun dismissInspectObject() {
        _uiState.update { it.copy(inspectObject = null) }
    }

    fun toggleFavorite(info: ObjectInformation) {
        viewModelScope.launch {
            val current = _uiState.value.isInspectFavorite
            repository.toggleFavorite(info, current)
            _uiState.update { it.copy(isInspectFavorite = !current) }
        }
    }
}
