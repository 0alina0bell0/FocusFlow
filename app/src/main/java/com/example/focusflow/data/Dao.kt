package com.example.focusflow.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

//    @Query("SELECT * FROM entities")
@Query("SELECT * FROM entities ORDER BY priority DESC, id DESC")
    fun getAllTasks(): Flow<List<Entity>>

    @Update
    suspend fun updateTasks(entity: Entity)

    @Insert
    suspend fun addTasks(entity: Entity): Long

    @Delete
    suspend fun deleteTasks(entity: Entity)

    @Query("SELECT * FROM entities WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long): Entity?

    @Query(
        """
    SELECT * FROM entities
    WHERE isCompleted = 0 AND reminder > :now
    """
    )
    suspend fun getFutureReminders(now: Long): List<Entity>
}
