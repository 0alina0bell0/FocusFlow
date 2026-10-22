package com.example.focusflow.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.focusflow.navigation.Routes

@Composable
fun NewPasswordScreen(
    navController: NavController,
    viewModel: NewPasswordViewModel = hiltViewModel()
){

    val state = viewModel.state.collectAsStateWithLifecycle().value

    var newPassword by remember { mutableStateOf("") }

    var repeatedPassword by remember { mutableStateOf("") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(
                    navController.graph.startDestinationId
                ) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Новый пароль",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = {
                newPassword = it
            },
            label = {
                Text("Новый пароль")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = repeatedPassword,
            onValueChange = {
                repeatedPassword = it
            },
            label = {
                Text("Повторите пароль")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        )

        state.error?.let { error ->
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updatePassword(
                    newPassword = newPassword,
                    repeatedPassword = repeatedPassword
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled =
                !state.isLoading &&
                        newPassword.isNotBlank() &&
                        repeatedPassword.isNotBlank()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Сохранить новый пароль")
            }
        }
    }

}