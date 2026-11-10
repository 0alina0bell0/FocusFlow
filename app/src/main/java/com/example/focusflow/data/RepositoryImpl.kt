package com.example.focusflow.data

import com.example.focusflow.domain.Repository
import com.example.focusflow.notifications.ReminderScheduler
import com.example.focusflow.notifications.TaskNotifications
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val reminderScheduler: ReminderScheduler,
    private val notifications: TaskNotifications
): Repository {

    override fun getAllTasks(): Flow<List<Entity>> {
        return dao.getAllTasks()
    }

    override suspend fun updateTasks(task: Entity) {
        dao.updateTasks(task)

        notifications.cancel(task.id)
        reminderScheduler.schedule(task)
    }

    override suspend fun addTasks(task: Entity) {
        val id = dao.addTasks(task)

        reminderScheduler.schedule(
            task.copy(id = id)
        )
    }

    override suspend fun deleteTasks(taskId: Entity) {
        dao.deleteTasks(taskId)

        reminderScheduler.cancel(taskId.id)
        notifications.cancel(taskId.id)
    }
}
