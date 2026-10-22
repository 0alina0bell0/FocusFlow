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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    private val recoveryLinkState =
        MutableStateFlow(RecoveryLinkState.IDLE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        handleRecoveryIntent(intent)

        setContent {
            val recoveryState by
            recoveryLinkState.collectAsStateWithLifecycle()

            FocusFlowTheme {
                AppNavGraph(
                    recoveryLinkState = recoveryState,
                    onRecoveryNavigationHandled = {
                        recoveryLinkState.value =
                            RecoveryLinkState.IDLE
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        handleRecoveryIntent(intent)
    }

    private fun handleRecoveryIntent(intent: Intent) {
        val uri = intent.data ?: return

        val isRecoveryLink =
            uri.scheme == "focusflow" &&
                    uri.host == "reset-password"

        if (!isRecoveryLink) {
            return
        }

        recoveryLinkState.value =
            RecoveryLinkState.PROCESSING

        supabaseClient.handleDeeplinks(
            intent = intent,

            onSessionSuccess = {
                lifecycleScope.launch {
                    recoveryLinkState.value =
                        RecoveryLinkState.READY
                }
            },

            onError = { error ->
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
