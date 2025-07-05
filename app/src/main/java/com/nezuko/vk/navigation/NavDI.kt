package com.nezuko.vk.navigation

import com.nezuko.domain.repository.Navigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavBindsModule {
    @Binds
    @Singleton
    fun bindNavigation(navigationImpl: NavigationImpl): Navigation
}
