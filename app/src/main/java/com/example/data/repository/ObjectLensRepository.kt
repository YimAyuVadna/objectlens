package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.FavoriteEntity
import com.example.data.local.ObjectInformation
import com.example.data.local.ObjectKnowledgeBase
import com.example.data.local.ScanDao
import com.example.data.local.ScanDetectionEntity
import com.example.data.local.ScanRecordEntity
import com.example.ml.DetectedObject
import kotlinx.coroutines.flow.Flow

class ObjectLensRepository(private val scanDao: ScanDao) {

    val allScans: Flow<List<ScanRecordEntity>> = scanDao.getAllScans()
    val allFavorites: Flow<List<FavoriteEntity>> = scanDao.getAllFavorites()

    fun searchScans(query: String): Flow<List<ScanRecordEntity>> {
        return scanDao.searchScans(query)
    }

    suspend fun getDetectionsForScan(scanId: Long): List<ScanDetectionEntity> {
        return scanDao.getDetectionsForScan(scanId)
    }

    suspend fun saveScan(
        primaryObject: String,
        primaryCategory: String,
        detections: List<DetectedObject>,
        notes: String = "",
        sceneType: String = "camera"
    ): Long {
        val avgConfidence = if (detections.isNotEmpty()) {
            detections.map { it.confidence }.average().toFloat()
        } else {
            0.0f
        }

        val record = ScanRecordEntity(
            primaryObject = primaryObject,
            primaryCategory = primaryCategory,
            detectionsCount = detections.size,
            avgConfidence = avgConfidence,
            notes = notes,
            sceneType = sceneType
        )

        val detectionEntities = detections.map {
            ScanDetectionEntity(
                scanId = 0,
                label = it.label,
                displayName = it.displayName,
                confidence = it.confidence,
                category = it.category,
                left = it.normalizedLeft,
                top = it.normalizedTop,
                right = it.normalizedRight,
                bottom = it.normalizedBottom
            )
        }

        return scanDao.saveScanWithDetections(record, detectionEntities)
    }

    suspend fun deleteScan(scanId: Long) {
        scanDao.deleteScanById(scanId)
    }

    suspend fun clearHistory() {
        scanDao.clearAllScans()
    }

    fun isFavorite(objectId: String): Flow<Boolean> {
        return scanDao.isFavorite(objectId)
    }

    suspend fun toggleFavorite(objectInfo: ObjectInformation, isFav: Boolean) {
        if (isFav) {
            scanDao.removeFavorite(objectInfo.id)
        } else {
            scanDao.addFavorite(
                FavoriteEntity(
                    objectId = objectInfo.id,
                    objectName = objectInfo.name,
                    category = objectInfo.category
                )
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ObjectLensRepository? = null

        fun getInstance(database: AppDatabase): ObjectLensRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = ObjectLensRepository(database.scanDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
