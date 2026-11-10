package com.example.focusflow.notifications

import android.app.AlarmManager
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
class RestoreRemindersReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dao: Dao

    @Inject
    lateinit var scheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        val supportedAction =
            intent.action == Intent.ACTION_BOOT_COMPLETED ||
                    intent.action == Intent.ACTION_MY_PACKAGE_REPLACED ||
                    intent.action ==
                    AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED

        if (!supportedAction) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(8_000L.milliseconds) {
                    dao.getFutureReminders(System.currentTimeMillis())
                        .forEach { task ->
                            scheduler.schedule(task)
                        }
                }
            } catch (e: Exception) {
                Log.e(
                    "RestoreReminders",
                    "Не удалось восстановить напоминания",
                    e
                )
            } finally {
                pendingResult.finish()
            }
        }
    }
}