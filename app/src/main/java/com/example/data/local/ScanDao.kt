package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class ScanWithDetections(
    val scan: ScanRecordEntity,
    val detections: List<ScanDetectionEntity>
)

@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanRecord(record: ScanRecordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetections(detections: List<ScanDetectionEntity>)

    @Transaction
    suspend fun saveScanWithDetections(
        record: ScanRecordEntity,
        detections: List<ScanDetectionEntity>
    ): Long {
        val scanId = insertScanRecord(record)
        val attached = detections.map { it.copy(scanId = scanId) }
        insertDetections(attached)
        return scanId
    }

    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_records WHERE primaryObject LIKE '%' || :query || '%' OR primaryCategory LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchScans(query: String): Flow<List<ScanRecordEntity>>

    @Query("SELECT * FROM scan_detections WHERE scanId = :scanId")
    suspend fun getDetectionsForScan(scanId: Long): List<ScanDetectionEntity>

    @Query("DELETE FROM scan_records WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM scan_records")
    suspend fun clearAllScans()

    // Favorites
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE objectId = :objectId)")
    fun isFavorite(objectId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE objectId = :objectId")
    suspend fun removeFavorite(objectId: String)

    // Learned Labels (self-learning)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearnedLabel(entity: LearnedLabelEntity)

    @Query("SELECT * FROM learned_labels")
    suspend fun getAllLearnedLabels(): List<LearnedLabelEntity>

    @Query("SELECT * FROM learned_labels WHERE label = :label")
    suspend fun getLearnedLabel(label: String): LearnedLabelEntity?

    @Query("DELETE FROM learned_labels")
    suspend fun clearLearnedLabels()
}

