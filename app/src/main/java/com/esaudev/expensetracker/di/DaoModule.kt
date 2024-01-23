package com.esaudev.expensetracker.di

import com.esaudev.expensetracker.data.local.database.ExpenseTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun providesExpenseDao(
        database: ExpenseTrackerDatabase
    ) = database.expenseDao()
}
