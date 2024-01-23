package com.esaudev.expensetracker.domain.repository

import com.esaudev.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun observeByMonth(monthValue: String): Flow<List<Expense>>

    suspend fun upsert(expense: Expense)
}
