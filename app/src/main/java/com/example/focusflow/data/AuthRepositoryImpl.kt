package com.example.focusflow.data

import com.example.focusflow.data.auth.AuthApi
import com.example.focusflow.data.auth.AuthRequest
import com.example.focusflow.data.auth.RefreshRequest
import com.example.focusflow.data.auth.TokenStorage
import com.example.focusflow.domain.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import retrofit2.HttpException
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
    private val client: SupabaseClient
): AuthRepository {

    override suspend fun register(email: String, password: String) {
        val response = authApi.register(
            AuthRequest(
                email = email,
                password = password
            )
        )

        val accessToken = response.accessToken
        val refreshToken = response.refreshToken

        if (
            accessToken != null &&
            refreshToken != null
        ) {

            tokenStorage.saveTokens(
                accessToken,
                refreshToken
            )
        }
    }

    override suspend fun login(email: String, password: String) {
        val response = authApi.signIn(
            request = AuthRequest(
                email = email,
                password = password
            )
        )

        val accessToken =
            response.accessToken
                ?: error("Access token не получен")

        val refreshToken =
            response.refreshToken
                ?: error("Refresh token не получен")


        tokenStorage.saveTokens(
            accessToken,
            refreshToken
        )
    }

    override suspend fun logout() {
        tokenStorage.clear()
    }

    override fun isLoggedIn(): Boolean {
        return client.auth.currentSessionOrNull() != null
    }

    override suspend fun requestPasswordReset(email: String) {
        client.auth.resetPasswordForEmail(email)
    }

    override suspend fun getCurrentUser(): UserDto {
        val accessToken =
            tokenStorage.getAccessToken()
                ?: error("Пользователь не авторизован")

        try {

            return authApi.getUser(
                "Bearer $accessToken"
            )

        } catch (e: HttpException) {

            if (e.code() != 401) {
                throw e
            }
        }


        val refreshToken =
            tokenStorage.getRefreshToken()
                ?: error("Refresh token отсутствует")


        val refreshed =
            authApi.refreshToken(
                request = RefreshRequest(
                    refreshToken
                )
            )


        val newAccessToken =
            refreshed.accessToken
                ?: error("Новый access token не получен")

        val newRefreshToken =
            refreshed.refreshToken
                ?: error("Новый refresh token не получен")


        tokenStorage.saveTokens(
            newAccessToken,
            newRefreshToken
        )

        try {

            return authApi.getUser(
                "Bearer $newAccessToken"
            )

        } catch (e: HttpException) {

            if (e.code() == 401) {
                tokenStorage.clear()
            }

            throw e
        }
    }
}






/*
class AuthRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
): AuthRepository {

    override suspend fun register(email: String, password: String){
        client.auth.signUpWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    override suspend fun login(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    override suspend fun logout() {
        client.auth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return client.auth.currentSessionOrNull() != null
    }
}*/
