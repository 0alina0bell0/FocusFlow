package com.example.focusflow.data

import com.example.focusflow.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val dao: Dao
): Repository {

    override fun getAllTasks(): Flow<List<Entity>> {
        return dao.getAllTasks()
    }

    override suspend fun updateTasks(task: Entity) {
        dao.updateTasks(task)
    }

    override suspend fun addTasks(task: Entity) {
        dao.addTasks(task)
    }

    override suspend fun deleteTasks(taskId: Entity) {
        dao.deleteTasks(taskId)
    }
}
