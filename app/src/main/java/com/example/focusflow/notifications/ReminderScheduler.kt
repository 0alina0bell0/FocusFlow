package com.example.focusflow.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.example.focusflow.data.Entity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager =
        context.getSystemService(AlarmManager::class.java)

    fun canScheduleExact(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                alarmManager.canScheduleExactAlarms()
    }

    fun schedule(task: Entity) {

        cancel(task.id)

        val time = task.reminder ?: return

        if (task.isCompleted || time <= System.currentTimeMillis()) {
            return
        }

        val operation = pendingIntent(task.id)

        if (canScheduleExact()) {
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    operation
                )
                return
            } catch (_: SecurityException){

            }
        }

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            operation
        )
    }

    fun cancel(taskId: Long) {
        val operation = pendingIntent(taskId)
        alarmManager.cancel(operation)
        operation.cancel()
    }

    private fun pendingIntent(taskId: Long): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            data = Uri.parse("focusflow://task-reminder/$taskId")
            putExtra(ReminderReceiver.EXTRA_TASK_ID, taskId)
        }

        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }
}