package com.esaudev.expensetracker.domain.repository

import com.esaudev.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun observeByMonth(monthValue: String): Flow<List<Expense>>

    fun observeSumByMonth(monthValue: String): Flow<String>

    suspend fun upsert(expense: Expense)

    suspend fun deleteById(id: String)
}
