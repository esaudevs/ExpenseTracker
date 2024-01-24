package com.esaudev.expensetracker.ui.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.usecase.SaveUserNameUseCase
import com.esaudev.expensetracker.domain.usecase.ValidateUserNameUseCase
import com.esaudev.expensetracker.util.UiText
import com.esaudev.expensetracker.util.UiTopLevelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val validateUserNameUseCase: ValidateUserNameUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<OnboardingUiState> =
        MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiTopLevelEvent = Channel<UiTopLevelEvent>()
    val uiTopLevelEvent = _uiTopLevelEvent.receiveAsFlow()

    fun onNameChanged(name: String) {
        viewModelScope.launch {
            if (name.length <= NAME_MAX_LENGTH) {
                updateState(
                    userName = name,
                    errorMessage = null
                )

                return@launch
            }
        }
    }

    private fun updateState(
        userName: String = _uiState.value.name,
        errorMessage: UiText? = null,
        isLoading: Boolean = false
    ) {
        _uiState.update {
            it.copy(
                name = userName,
                nameError = errorMessage,
                isLoading = isLoading
            )
        }
    }

    fun onContinue() {
        viewModelScope.launch {
            val nameValidation =
                validateUserNameUseCase.execute(_uiState.value.name)

            if (!nameValidation.successful) {
                updateState(
                    errorMessage = nameValidation.errorMessage
                )
                return@launch
            }

            updateState(
                isLoading = true
            )

            async {
                saveUserNameUseCase.execute(
                    userName = _uiState.value.name
                )
            }.await()

            updateState(
                isLoading = false
            )

            _uiTopLevelEvent.send(UiTopLevelEvent.Success)
        }
    }

    data class OnboardingUiState(
        val name: String = "",
        val isLoading: Boolean = false,
        val nameError: UiText? = null
    )
}
