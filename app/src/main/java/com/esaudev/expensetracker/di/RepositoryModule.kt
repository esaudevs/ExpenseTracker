package com.esaudev.expensetracker.di

import com.esaudev.expensetracker.data.repository.DefaultExpenseRepository
import com.esaudev.expensetracker.data.repository.ProtoUserRepository
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import com.esaudev.expensetracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsUserRepository(
        protoUserRepository: ProtoUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindsExpenseRepository(
        defaultExpenseRepository: DefaultExpenseRepository
    ): ExpenseRepository
}
