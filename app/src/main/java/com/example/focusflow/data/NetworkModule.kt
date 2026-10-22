package com.example.focusflow.data

import com.example.focusflow.BuildConfig
import com.example.focusflow.data.auth.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providerOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                SupabaseApiKeyInterceptor(
                    BuildConfig.SUPABASE_KEY
                )
            )
            .build()


    @Singleton
    @Provides
    fun providerRetrofit(okHttpClient: OkHttpClient): Retrofit =
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


    @Singleton
    @Provides
    fun providerAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(
            AuthApi::class.java
        )
}