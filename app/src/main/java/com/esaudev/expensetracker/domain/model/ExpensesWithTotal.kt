package com.esaudev.expensetracker.domain.model

data class ExpensesWithTotal(
    val expenses: List<Expense>,
    val total: String
)
