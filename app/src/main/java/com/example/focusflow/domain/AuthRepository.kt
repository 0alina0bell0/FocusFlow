package com.example.focusflow.domain


interface AuthRepository {

    suspend fun register(email: String, password: String)

    suspend fun login(email: String, password: String)

    suspend fun logout()

    fun isLoggedIn(): Boolean
}