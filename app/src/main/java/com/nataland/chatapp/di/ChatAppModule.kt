package com.nataland.chatapp.di

import com.nataland.chatapp.network.ChatRepository
import com.nataland.chatapp.network.ChatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatAppModule {

    @Provides
    fun provideChatRepository(
        // Potential dependencies of this type
    ): ChatRepository {
        return ChatRepositoryImpl()
    }
}
