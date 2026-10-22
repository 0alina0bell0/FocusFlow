package com.example.focusflow.presentation.login

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val timerSeconds: Int = 0
)

