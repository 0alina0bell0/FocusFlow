package com.example.focusflow.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focusflow.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow(AuthUiState())

    val state =
        _state.asStateFlow()

    private var timerJob: Job? = null

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                _state.value = _state.value.copy(timerSeconds = i)
                delay(1000.milliseconds)
            }
        }
    }

    fun resetPassword(email: String) {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                repository.requestPasswordReset(email.trim())

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }

                startTimer()

            } catch (e: Exception) {

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }

    }
}