package com.example.focusflow.data.auth

import com.example.focusflow.data.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(

    @SerialName("access_token")
    val accessToken: String? = null,

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    @SerialName("expires_in")
    val expiresIn: Long? = null,

    val user: UserDto? = null
)
