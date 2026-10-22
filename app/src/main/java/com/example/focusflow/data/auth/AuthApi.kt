package com.example.focusflow.data.auth

import com.example.focusflow.data.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthApi {

    @POST("auth/v1/signup")
    suspend fun register(
        @Body request: AuthRequest
    ): AuthResponse

    @POST("auth/v1/token")
    suspend fun signIn(
        @Query("grant_type") grantType: String = "password",
        @Body request: AuthRequest
    ): AuthResponse

    @POST("auth/v1/token")
    suspend fun refreshToken(
        @Query("grant_type") grantType: String = "refresh_token",
        @Body request: RefreshRequest
    ): AuthResponse

    @GET("auth/v1/user")
    suspend fun getUser(
        @Header("Authorization") authorization: String
    ): UserDto

}