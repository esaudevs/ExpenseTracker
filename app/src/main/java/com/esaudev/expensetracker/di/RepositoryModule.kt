package com.esaudev.expensetracker.di

import com.esaudev.expensetracker.data.repository.ProtoUserRepository
import com.esaudev.expensetracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(
        protoUserRepository: ProtoUserRepository
    ): UserRepository
}
