package com.esaudev.expensetracker.domain.usecase

import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class CreateExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    suspend fun execute(expense: Expense) {
        expenseRepository.upsert(expense = expense)
    }
}
