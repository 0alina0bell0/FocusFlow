package com.example.focusflow.data

import com.example.focusflow.domain.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject

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
}