package com.example.focusflow

import android.app.Application
import com.example.focusflow.data.AuthRepositoryImpl
import com.example.focusflow.data.NetworkModule
import com.example.focusflow.data.SupabaseProvider.client
import com.example.focusflow.data.auth.TokenStorage
import com.example.focusflow.domain.AuthRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FocusFlowApplication: Application(){
    val authRepository: AuthRepository by lazy {

        AuthRepositoryImpl(
            authApi = NetworkModule.authApi,
            tokenStorage = TokenStorage(this),
            client = client
        )
    }
}