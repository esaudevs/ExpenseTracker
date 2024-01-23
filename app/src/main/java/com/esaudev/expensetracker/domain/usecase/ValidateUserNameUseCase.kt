package com.esaudev.expensetracker.domain.usecase

import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ext.hasSpecialCharacters
import com.esaudev.expensetracker.util.UiText
import javax.inject.Inject

class ValidateUserNameUseCase @Inject constructor() {

    fun execute(userName: String): ValidationResult {
        if (userName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.onboarding__empty_name_string)
            )
        }

        if (userName.hasSpecialCharacters()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.onboarding__name_bad_formatted)
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
