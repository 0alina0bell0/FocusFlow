package com.example.focusflow.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.focusflow.data.Dao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dao: Dao

    @Inject
    lateinit var notifications: TaskNotifications

    companion object {
        const val EXTRA_TASK_ID = "task_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        if (taskId == -1L) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(8_000L.milliseconds) {
                    val task = dao.getTaskById(taskId)
                        ?: return@withTimeout

                    val reminder = task.reminder
                        ?: return@withTimeout

                    if (
                        !task.isCompleted &&
                        reminder <= System.currentTimeMillis()
                    ) {
                        notifications.show(task)
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "ReminderReceiver",
                    "Не удалось показать напоминание",
                    e
                )
            } finally {
                pendingResult.finish()
            }
        }
    }
}