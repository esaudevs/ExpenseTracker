package com.esaudev.expensetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.repository.UserRepository
import com.esaudev.expensetracker.proto.UserModel
import com.esaudev.expensetracker.ui.features.onboarding.navigation.onboardingRoute
import com.esaudev.expensetracker.ui.features.tracker.navigation.trackerRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userRepository.user.map {
        MainActivityUiState.Success(
            userModel = it,
            startDestination = if (it.name.isNullOrEmpty()) onboardingRoute else trackerRoute
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val userModel: UserModel,
        val startDestination: String
    ) : MainActivityUiState
}
