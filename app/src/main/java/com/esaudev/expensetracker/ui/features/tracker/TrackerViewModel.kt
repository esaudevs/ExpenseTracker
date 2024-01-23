package com.esaudev.expensetracker.ui.features.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.repository.ExpenseRepository
import com.esaudev.expensetracker.domain.repository.UserRepository
import com.esaudev.expensetracker.ext.monthValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val uiState: StateFlow<TrackerUiState> =
        combine(
            userRepository.user.distinctUntilChanged(),
            expenseRepository.observeSumByMonth(
                LocalDateTime.now().monthValue()
            ).distinctUntilChanged(),
            expenseRepository.observeByMonth(
                LocalDateTime.now().monthValue()
            ).distinctUntilChanged()
        ) { userModel, monthlyExpense, expenses ->
            TrackerUiState.WithContent(
                userName = userModel.name,
                monthlyExpenses = monthlyExpense,
                expenses = expenses
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TrackerUiState.Loading
        )

    sealed interface TrackerUiState {
        data object Loading : TrackerUiState
        data class WithContent(
            val userName: String,
            val monthlyExpenses: String,
            val expenses: List<Expense>
        ) : TrackerUiState
    }
}
