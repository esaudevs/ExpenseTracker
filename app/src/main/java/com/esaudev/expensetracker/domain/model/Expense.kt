package com.esaudev.expensetracker.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val concept: String,
    val amount: String,
    val paidAt: LocalDateTime
)
