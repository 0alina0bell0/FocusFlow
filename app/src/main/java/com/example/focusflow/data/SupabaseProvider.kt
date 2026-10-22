package com.example.focusflow.data

import com.example.focusflow.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseProvider {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ){
        install(Auth){
            scheme = "focusflow"
            host = "reset-password"
        }
    }
}