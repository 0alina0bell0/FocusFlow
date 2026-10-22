package com.example.focusflow.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focusflow.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())

    val state = _state.asStateFlow()


    fun updatePassword(
        newPassword: String,
        repeatedPassword: String
    ) {
        when {
            newPassword.isBlank() -> {
                showError("Введите новый пароль")
                return
            }

            repeatedPassword.isBlank() -> {
                showError("Повторите новый пароль")
                return
            }

            newPassword != repeatedPassword -> {
                showError("Пароли не совпадают")
                return
            }

            newPassword.length < 8 -> {
                showError(
                    "Пароль должен содержать не менее 8 символов"
                )
                return
            }
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isSuccess = false,
                    error = null
                )
            }

            try {
                repository.updatePassword(newPassword)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error =
                            e.message
                                ?: "Не удалось изменить пароль"
                    )
                }
            }
        }
    }

    private fun showError(message: String) {
        _state.update {
            it.copy(
                isLoading = false,
                isSuccess = false,
                error = message
            )
        }
    }
}

