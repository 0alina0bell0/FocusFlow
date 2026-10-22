package com.example.focusflow.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusflow.ui.theme.MutePink
import com.example.focusflow.ui.theme.Pink80

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = hiltViewModel()
){

    val state = viewModel.state.collectAsStateWithLifecycle().value
    var email by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MutePink),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Сброс пароля", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                        tint = Pink80
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Pink80
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.timerSeconds > 0) {
                Text(
                    text = "Повторить попытку через ${state.timerSeconds} секунд.",
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.resetPassword(email)
                },
                enabled = state.timerSeconds == 0
            ) {
                Text("Сброс")
            }

        }
    }
}