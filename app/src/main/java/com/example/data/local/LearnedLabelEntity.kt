package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_labels")
data class LearnedLabelEntity(
    @PrimaryKey val label: String,
    val sightingCount: Int = 0,
    val confirmationCount: Int = 0,
    val lastSeen: Long = System.currentTimeMillis(),
    val coOccurrences: String = "" // JSON-encoded Map<String, Int>
)
