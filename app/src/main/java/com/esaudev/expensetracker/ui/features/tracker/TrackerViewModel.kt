package com.esaudev.expensetracker.ui.features.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import com.esaudev.expensetracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<TrackerUiState> =
        MutableStateFlow(TrackerUiState.Loading)
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    fun getExpensesByQuery() {
        viewModelScope.launch {
            combine(
                userRepository.user,
                expenseRepository.observeByMonth(),
                expenseRepository.dateQuery
            ) { userModel, expensesWithTotal, date ->
                TrackerUiState.WithContent(
                    userName = userModel.name,
                    monthlyExpenses = expensesWithTotal.total,
                    expenses = expensesWithTotal.expenses,
                    date = date
                )
            }.collectLatest { withContentState ->
                _uiState.update {
                    withContentState
                }
            }
        }
    }

    fun onNextMonth() {
        expenseRepository.queryToNextMonth()
    }

    fun onPreviousMonth() {
        expenseRepository.queryToPreviousMonth()
    }

    fun onDeleteExpense(id: String) {
        viewModelScope.launch {
            expenseRepository.deleteById(id)
        }
    }
}

sealed interface TrackerUiState {
    data object Loading : TrackerUiState
    data class WithContent(
        val userName: String,
        val monthlyExpenses: String,
        val expenses: List<Expense>,
        val date: LocalDateTime
    ) : TrackerUiState
}
