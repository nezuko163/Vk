package com.nezuko.data.di

import com.nezuko.data.impl.FilesContentRepositoryImpl
import com.nezuko.data.impl.FilesMetadataRepositoryImpl
import com.nezuko.domain.repository.FilesContentRepository
import com.nezuko.domain.repository.FilesMetadataRepository
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
    abstract fun fileMetadataRepo(impl: FilesMetadataRepositoryImpl): FilesMetadataRepository

    @Binds
    @Singleton
    abstract fun fileReadWriteRepo(impl: FilesContentRepositoryImpl): FilesContentRepository
}