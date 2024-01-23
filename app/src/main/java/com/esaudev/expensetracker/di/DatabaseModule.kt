package com.esaudev.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.esaudev.expensetracker.data.local.database.ExpenseTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesExpenseTrackerDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ExpenseTrackerDatabase::class.java,
        "expense_tracker_database"
    )
        .fallbackToDestructiveMigration()
        .build()
}
