package com.esaudev.expensetracker.domain.usecase

import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.util.UiText
import javax.inject.Inject

class ValidateAmountUseCase @Inject constructor() {

    fun execute(amount: String): ValidationResult {
        if (amount.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.create_expense__empty_amount_error)
            )
        }

        if (amount.toDoubleOrNull() == null || amount.toDouble() <= 0) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.create_expense__empty_amount_error)
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}
