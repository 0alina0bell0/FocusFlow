package com.example.focusflow.presentation.login

import androidx.lifecycle.ViewModel
import com.example.focusflow.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {


}