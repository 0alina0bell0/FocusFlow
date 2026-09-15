package com.example.focusflow.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM entities")
    fun getAllTasks(): Flow<List<Entity>>

    @Update
    suspend fun updateTasks(entity: Entity)

    @Insert
    suspend fun addTasks(entity: Entity)

    @Delete
    suspend fun deleteTasks(entity: Entity)
}
