package com.example.focusflow.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun AppNavGraph(
    recoveryLinkState: RecoveryLinkState,
    onRecoveryNavigationHandled: () -> Unit
) {
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
        startDestination = Routes.LOGIN
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
            MainListScreen(navController = navController)
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
