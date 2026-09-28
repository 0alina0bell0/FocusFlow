package com.example.focusflow.data

import com.example.focusflow.BuildConfig
import com.example.focusflow.data.auth.AuthApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                SupabaseApiKeyInterceptor(
                    BuildConfig.SUPABASE_KEY
                )
            )
            .build()


    private val retrofit =
        Retrofit.Builder()
            .baseUrl(
                BuildConfig.SUPABASE_URL
            )
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()


    val authApi: AuthApi =
        retrofit.create(
            AuthApi::class.java
        )
}