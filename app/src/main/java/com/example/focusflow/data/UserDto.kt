package com.example.focusflow.data

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,

    val email: String? = null
)
