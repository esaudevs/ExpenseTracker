package com.esaudev.expensetracker.ui.features.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.usecase.CreateExpenseUseCase
import com.esaudev.expensetracker.util.UiTopLevelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateExpenseViewModel @Inject constructor(
    private val createExpenseUseCase: CreateExpenseUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateExpenseUiState> =
        MutableStateFlow(CreateExpenseUiState())
    val uiState: StateFlow<CreateExpenseUiState> = _uiState.asStateFlow()

    private val _uiTopLevelEvent = Channel<UiTopLevelEvent>()
    val uiTopLevelEvent = _uiTopLevelEvent.receiveAsFlow()

    fun onAmountChange(amount: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    amount = amount
                )
            }
        }
    }

    fun onConceptChange(concept: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    concept = concept
                )
            }
        }
    }

    fun onCreateExpense() {
        viewModelScope.launch {
            createExpenseUseCase.execute(
                expense = Expense(
                    concept = _uiState.value.concept,
                    amount = _uiState.value.amount,
                    paidAt = _uiState.value.paidAt
                )
            )

            _uiTopLevelEvent.send(UiTopLevelEvent.Success)
        }
    }

    fun clearUiState() {
        viewModelScope.launch {
            _uiState.update {
                CreateExpenseUiState()
            }
        }
    }

    data class CreateExpenseUiState(
        val amount: String = "",
        val concept: String = "",
        val paidAt: LocalDateTime = LocalDateTime.now(),
        val isLoading: Boolean = false
    )
}
