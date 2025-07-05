package com.nezuko.data.di

import com.nezuko.data.impl.FilesRepositoryImpl
import com.nezuko.domain.repository.FilesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun fileRepo(impl: FilesRepositoryImpl): FilesRepository
}