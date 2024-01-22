package com.esaudev.expensetracker.ui.features.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.expensetracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<TrackerUiState> = userRepository.user
        .map {
            TrackerUiState.WithContent(
                userName = it.name
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TrackerUiState.Loading
        )

    sealed interface TrackerUiState {
        data object Loading : TrackerUiState
        data class WithContent(
            val userName: String
        ) : TrackerUiState
    }
}
