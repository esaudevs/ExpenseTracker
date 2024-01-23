package com.esaudev.expensetracker.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.esaudev.expensetracker.domain.model.Expense
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val amount: BigDecimal,
    val concept: String,
    val paidAt: LocalDateTime
)

fun ExpenseEntity.toExpense(): Expense {
    return Expense(
        id = id,
        concept = concept,
        amount = amount.toString(),
        paidAt = paidAt
    )
}

fun Expense.toExpenseEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        concept = concept,
        amount = BigDecimal(amount),
        paidAt = paidAt
    )
}
