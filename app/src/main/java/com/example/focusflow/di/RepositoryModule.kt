package com.example.focusflow.di

import com.example.focusflow.data.AuthRepositoryImpl
import com.example.focusflow.data.RepositoryImpl
import com.example.focusflow.domain.AuthRepository
import com.example.focusflow.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

}
