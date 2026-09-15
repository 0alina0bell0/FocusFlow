package com.example.focusflow.domain

import com.example.focusflow.data.Entity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllTasks(): Flow<List<Entity>>

    suspend fun updateTasks(task: Entity)
    suspend fun addTasks(task: Entity)
    suspend fun deleteTasks(taskId: Entity)
}