package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnedObjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(learnedObject: LearnedObjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(learnedObjects: List<LearnedObjectEntity>)

    @Query("SELECT COUNT(*) FROM learned_objects")
    suspend fun getCount(): Int

    @Update
    suspend fun update(learnedObject: LearnedObjectEntity)

    @Query("SELECT * FROM learned_objects ORDER BY lastRecognizedAt DESC")
    fun getAllLearnedObjectsFlow(): Flow<List<LearnedObjectEntity>>

    @Query("SELECT * FROM learned_objects ORDER BY lastRecognizedAt DESC")
    suspend fun getAllLearnedObjects(): List<LearnedObjectEntity>

    @Query("SELECT * FROM learned_objects WHERE id = :id LIMIT 1")
    suspend fun getLearnedObjectById(id: String): LearnedObjectEntity?

    @Query("DELETE FROM learned_objects WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM learned_objects")
    suspend fun clearAll()
}
