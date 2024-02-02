package com.esaudev.expensetracker.ui.helpers

data class DialogWithContentState<T>(
    val shouldBeVisible: Boolean,
    val content: T?
)
