package com.example.focusflow.navigation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.focusflow.data.RecoveryLinkState
import com.example.focusflow.presentation.MainListScreen.CreateItemScreen
import com.example.focusflow.presentation.MainListScreen.MainListScreen
import com.example.focusflow.presentation.MainListScreen.ViewModelListScreen
import com.example.focusflow.presentation.login.LoginScreen
import com.example.focusflow.presentation.login.NewPasswordScreen
import com.example.focusflow.presentation.login.RegistrationScreen
import com.example.focusflow.presentation.login.ResetPasswordScreen
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


@Composable
fun AppNavGraph(
    recoveryLinkState: RecoveryLinkState,
    onRecoveryNavigationHandled: () -> Unit,
    startRoute: String,
    onLogout: suspend () -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val navController = rememberNavController()

    LaunchedEffect(recoveryLinkState) {
        if (recoveryLinkState == RecoveryLinkState.READY) {
            navController.navigate(Routes.NEW_PASSWORD) {
                launchSingleTop = true
            }
            onRecoveryNavigationHandled()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startRoute,
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.REGISTER) {
            RegistrationScreen(navController = navController)
        }

        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen()
        }

        composable(Routes.NEW_PASSWORD) {
            NewPasswordScreen(navController = navController)
        }

        composable(Routes.HOME) {
            MainListScreen(
                navController = navController,
                onLogoutClick = {
                    scope.launch {
                        try {
                            onLogout()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Не удалось выйти",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }

        composable(Routes.CREATE) {

                val viewModel: ViewModelListScreen = hiltViewModel()

                CreateItemScreen(

                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { task ->
                        viewModel.addTask(task)
                        navController.popBackStack()
                    }
                )
        }

    }

}
