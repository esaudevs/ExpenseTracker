package com.esaudev.expensetracker.ui.features.expenses.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.domain.usecase.CreateExpenseUseCase
import com.esaudev.expensetracker.domain.usecase.ValidateAmountUseCase
import com.esaudev.expensetracker.domain.usecase.ValidateConceptUseCase
import com.esaudev.expensetracker.util.UiText
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
    private val createExpenseUseCase: CreateExpenseUseCase,
    private val validateConceptUseCase: ValidateConceptUseCase,
    private val validateAmountUseCase: ValidateAmountUseCase
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
                    amount = amount,
                    amountError = null
                )
            }
        }
    }

    fun onConceptChange(concept: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    concept = concept,
                    conceptError = null
                )
            }
        }
    }

    fun onDateChange(paidAt: LocalDateTime) {
        _uiState.update {
            it.copy(
                paidAt = paidAt
            )
        }
    }

    fun onCreateExpense() {
        viewModelScope.launch {
            val conceptValidation =
                validateConceptUseCase.execute(_uiState.value.concept)

            val amountValidation =
                validateAmountUseCase.execute(_uiState.value.amount)

            if (!conceptValidation.successful || !amountValidation.successful) {
                _uiState.update {
                    it.copy(
                        amountError = amountValidation.errorMessage,
                        conceptError = conceptValidation.errorMessage
                    )
                }
                return@launch
            }

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
        val isLoading: Boolean = false,
        val amountError: UiText? = null,
        val conceptError: UiText? = null
    )
}
