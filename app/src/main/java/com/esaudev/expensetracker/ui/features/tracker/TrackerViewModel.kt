package com.esaudev.expensetracker.ui.features.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import com.esaudev.expensetracker.domain.repository.UserRepository
import com.esaudev.expensetracker.ext.monthValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _queryState: MutableStateFlow<ExpenseQueryState> =
        MutableStateFlow(ExpenseQueryState())
    val queryState: StateFlow<ExpenseQueryState> = _queryState.asStateFlow()

    private val _uiState: MutableStateFlow<TrackerUiState> =
        MutableStateFlow(TrackerUiState.Loading)
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    fun getExpensesByQuery() {
        viewModelScope.launch {
            queryState.collectLatest {
                combine(
                    userRepository.user.distinctUntilChanged(),
                    expenseRepository.observeSumByMonth(
                        it.date.monthValue()
                    ).distinctUntilChanged(),
                    expenseRepository.observeByMonth(
                        it.date.monthValue()
                    ).distinctUntilChanged()
                ) { userModel, monthlyExpenses, expenses ->
                    TrackerUiState.WithContent(
                        userName = userModel.name,
                        monthlyExpenses = monthlyExpenses,
                        expenses = expenses
                    )
                }.collectLatest { withContentState ->
                    _uiState.update {
                        withContentState
                    }
                }
            }
        }
    }

    fun onNextMonth() {
        viewModelScope.launch {
            _queryState.update {
                it.copy(
                    date = it.date.plusMonths(1)
                )
            }
        }
    }

    fun onPreviousMonth() {
        viewModelScope.launch {
            _queryState.update {
                it.copy(
                    date = it.date.minusMonths(1)
                )
            }
        }
    }
}

data class ExpenseQueryState(
    val date: LocalDateTime = LocalDateTime.now()
)

sealed interface TrackerUiState {
    data object Loading : TrackerUiState
    data class WithContent(
        val userName: String,
        val monthlyExpenses: String,
        val expenses: List<Expense>
    ) : TrackerUiState
}
