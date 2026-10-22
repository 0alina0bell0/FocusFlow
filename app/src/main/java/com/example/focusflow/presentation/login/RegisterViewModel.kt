package com.example.focusflow.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focusflow.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow(AuthUiState())

    val state =
        _state.asStateFlow()

    fun register(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                repository.register(
                    email,
                    password
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }

           } catch (e: HttpException) {
                val details = e.response()
                    ?.errorBody()
                    ?.string()

                _state.update {
                    it.copy(
                        isLoading = false,
                        error = details ?: "HTTP ${e.code()}"
                    )
                }
            }
            //            catch (e: Exception) {
//
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message
//                    )
//                }
//            }
        }
    }
}