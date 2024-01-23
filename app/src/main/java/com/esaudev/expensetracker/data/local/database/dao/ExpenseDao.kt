package com.esaudev.expensetracker.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.esaudev.expensetracker.data.local.database.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsert(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE strftime('%m', paidAt) = :monthValue")
    fun observeByMonth(
        monthValue: String
    ): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE strftime('%m', paidAt) = :monthValue")
    fun observeSumByMonth(
        monthValue: String
    ): Flow<BigDecimal>

    @Query("DELETE FROM expenses WHERE id = :id")
    fun deleteById(id: String)
}
