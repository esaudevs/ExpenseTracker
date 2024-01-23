package com.esaudev.expensetracker.data.repository

import com.esaudev.expensetracker.data.local.database.dao.ExpenseDao
import com.esaudev.expensetracker.data.local.database.model.toExpense
import com.esaudev.expensetracker.data.local.database.model.toExpenseEntity
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class DefaultExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    @Named("io") private val dispatcher: CoroutineDispatcher,
) : ExpenseRepository {
    override fun observeByMonth(monthValue: String): Flow<List<Expense>> {
        return expenseDao.observeByMonth(monthValue = monthValue).map { expenseEntityList ->
            expenseEntityList.map { expenseEntity ->
                expenseEntity.toExpense()
            }
        }
    }

    override suspend fun upsert(expense: Expense) {
        expenseDao.upsert(expense.toExpenseEntity())
    }

    override suspend fun deleteById(id: String) {
        withContext(dispatcher) {
            expenseDao.deleteById(id)
        }
    }

    override fun observeSumByMonth(monthValue: String): Flow<String> {
        return expenseDao.observeSumByMonth(monthValue = monthValue).map { amount ->
            amount.toString()
        }
    }
}
