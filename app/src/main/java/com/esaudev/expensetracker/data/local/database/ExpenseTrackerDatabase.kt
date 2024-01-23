package com.esaudev.expensetracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.esaudev.expensetracker.data.local.database.converters.BigDecimalConverter
import com.esaudev.expensetracker.data.local.database.converters.LocalDateTimeConverter
import com.esaudev.expensetracker.data.local.database.dao.ExpenseDao
import com.esaudev.expensetracker.data.local.database.model.ExpenseEntity

@TypeConverters(
    BigDecimalConverter::class,
    LocalDateTimeConverter::class
)
@Database(
    entities = [
        ExpenseEntity::class
    ],
    version = 1
)
abstract class ExpenseTrackerDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
}
