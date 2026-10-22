package com.example.focusflow.domain

import com.example.focusflow.data.UserDto


interface AuthRepository {

    suspend fun register(email: String, password: String)

    suspend fun login(email: String, password: String)

    suspend fun logout()

    fun isLoggedIn(): Boolean

    suspend fun requestPasswordReset(email: String)

    suspend fun updatePassword(newPassword: String)

    suspend fun getCurrentUser(): UserDto
}