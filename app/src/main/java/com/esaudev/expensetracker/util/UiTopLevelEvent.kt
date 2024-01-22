package com.esaudev.expensetracker.util

sealed class UiTopLevelEvent {
    data object Success : UiTopLevelEvent()
    data object NavigateUp : UiTopLevelEvent()
    data class RetrieveMessage(val message: UiText) : UiTopLevelEvent()
}
