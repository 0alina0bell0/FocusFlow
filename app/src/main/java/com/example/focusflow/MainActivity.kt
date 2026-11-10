package com.example.focusflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.focusflow.data.RecoveryLinkState
import com.example.focusflow.navigation.AppNavGraph
import com.example.focusflow.ui.theme.FocusFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.focusflow.notifications.TaskNotifications
import android.app.AlarmManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.example.focusflow.domain.AuthRepository
import com.example.focusflow.navigation.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var supabaseClient: SupabaseClient

    private val recoveryLinkState =
        MutableStateFlow(RecoveryLinkState.IDLE)

    @Inject
    lateinit var taskNotifications: TaskNotifications

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                Toast.makeText(
                    this,
                    "Без разрешения напоминания не будут отображаться",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskNotifications.createChannel()
        requestNotificationPermission()
        requestExactAlarmAccess()

        enableEdgeToEdge()
        handleRecoveryIntent(intent)

        val startRoute = if (authRepository.isLoggedIn()) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

        setContent {
            val recoveryState by
            recoveryLinkState.collectAsStateWithLifecycle()

            FocusFlowTheme {
                AppNavGraph(
                    recoveryLinkState = recoveryState,
                    onRecoveryNavigationHandled = {
                        recoveryLinkState.value = RecoveryLinkState.IDLE
                    },
                    startRoute = startRoute,
                    onLogout = {
                        authRepository.logout()
                    }
                )
            }
        }
    }

    private fun requestExactAlarmAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager =
                getSystemService(AlarmManager::class.java)

            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:$packageName")
                )

                startActivity(intent)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        handleRecoveryIntent(intent)
    }

    private fun handleRecoveryIntent(intent: Intent) {
        val uri = intent.data ?: return

        Log.d(
            "PasswordRecovery",
            "Получена ссылка: scheme=${uri.scheme}, host=${uri.host}"
        )

        val isRecoveryLink =
            uri.scheme == "focusflow" && uri.host == "reset-password"

        if (!isRecoveryLink) {
            return
        }

        recoveryLinkState.value =
            RecoveryLinkState.PROCESSING

        supabaseClient.handleDeeplinks(
            intent = intent,

            onSessionSuccess = {

                Log.d("PasswordRecovery", "Сессия восстановления получена")

                lifecycleScope.launch {
                    recoveryLinkState.value =
                        RecoveryLinkState.READY
                }
            },

            onError = { error ->

                Log.e(
                    "PasswordRecovery",
                    "Не удалось обработать ссылку",
                    error
                )

                lifecycleScope.launch {
                    recoveryLinkState.value =
                        RecoveryLinkState.IDLE

                    Toast.makeText(
                        this@MainActivity,
                        error.message
                            ?: "Ссылка восстановления недействительна",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
