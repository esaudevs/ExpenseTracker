package com.esaudev.expensetracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.esaudev.expensetracker.data.local.database.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsert(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE CAST(strftime('%m', paidAt) AS INTEGER) = :monthValue")
    fun observeByMonth(
        monthValue: Int
    ): Flow<List<ExpenseEntity>>

    @Query("DELETE FROM expenses WHERE id = :id")
    fun deleteById(id: String)
}
