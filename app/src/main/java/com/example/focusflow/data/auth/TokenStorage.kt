package com.example.focusflow.data.auth

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences =
        context.getSharedPreferences(
            "auth_preferences",
            Context.MODE_PRIVATE
        )


    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {

        preferences
            .edit {
                putString(
                    "access_token",
                    accessToken
                )
                    .putString(
                        "refresh_token",
                        refreshToken
                    )
            }
    }


    fun getAccessToken(): String? {

        return preferences.getString(
            "access_token",
            null
        )
    }


    fun getRefreshToken(): String? {

        return preferences.getString(
            "refresh_token",
            null
        )
    }


    fun clear() {

        preferences
            .edit {
                clear()
            }
    }
}