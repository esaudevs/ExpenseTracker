package com.esaudev.expensetracker.domain.repository

import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.model.ExpensesWithTotal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface ExpenseRepository {

    val dateQuery: StateFlow<LocalDateTime>

    fun queryToNextMonth()

    fun queryToPreviousMonth()

    fun observeByMonth(): Flow<ExpensesWithTotal>

    suspend fun upsert(expense: Expense)

    suspend fun deleteById(id: String)
}
