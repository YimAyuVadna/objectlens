package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_objects")
data class LearnedObjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String = "Learned",
    val featureVector: String, // Comma-separated floats representing visual fingerprint
    val sampleCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val lastRecognizedAt: Long = System.currentTimeMillis(),
    val notes: String = ""
)
