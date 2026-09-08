package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val primaryObject: String,
    val primaryCategory: String,
    val detectionsCount: Int,
    val avgConfidence: Float,
    val notes: String = "",
    val sceneType: String = "camera"
)

@Entity(
    tableName = "scan_detections",
    foreignKeys = [
        ForeignKey(
            entity = ScanRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["scanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("scanId")]
)
data class ScanDetectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scanId: Long,
    val label: String,
    val displayName: String,
    val confidence: Float,
    val category: String,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val objectId: String,
    val objectName: String,
    val category: String,
    val addedAt: Long = System.currentTimeMillis()
)
