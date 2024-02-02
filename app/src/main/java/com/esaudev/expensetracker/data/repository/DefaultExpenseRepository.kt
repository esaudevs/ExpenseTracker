package com.esaudev.expensetracker.data.repository

import com.esaudev.expensetracker.data.local.database.dao.ExpenseDao
import com.esaudev.expensetracker.data.local.database.model.toExpense
import com.esaudev.expensetracker.data.local.database.model.toExpenseEntity
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.model.ExpensesWithTotal
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class DefaultExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    @Named("io") private val dispatcher: CoroutineDispatcher
) : ExpenseRepository {

    private val _dateQuery = MutableStateFlow(LocalDateTime.now())
    override val dateQuery: StateFlow<LocalDateTime> = _dateQuery.asStateFlow()

    override fun queryToNextMonth() {
        _dateQuery.update {
            it.plusMonths(1)
        }
    }

    override fun queryToPreviousMonth() {
        _dateQuery.update {
            it.minusMonths(1)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeByMonth(): Flow<ExpensesWithTotal> {
        return dateQuery.flatMapLatest {
            expenseDao.observeByMonth(monthValue = it.monthValue).map { expenseEntityList ->
                ExpensesWithTotal(
                    expenses = expenseEntityList.map { expenseEntity ->
                        expenseEntity.toExpense()
                    },
                    total = expenseEntityList.sumOf { expenseEntity ->
                        expenseEntity.amount
                    }.toString()
                )
            }
        }.flowOn(dispatcher)
    }

    override suspend fun upsert(expense: Expense) {
        expenseDao.upsert(expense.toExpenseEntity())
    }

    override suspend fun deleteById(id: String) {
        withContext(dispatcher) {
            expenseDao.deleteById(id)
        }
    }
}
