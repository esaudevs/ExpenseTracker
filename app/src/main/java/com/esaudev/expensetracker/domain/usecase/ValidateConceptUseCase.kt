package com.esaudev.expensetracker.domain.usecase

import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.util.UiText
import javax.inject.Inject

class ValidateConceptUseCase @Inject constructor() {

    fun execute(concept: String): ValidationResult {
        if (concept.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.create_expense__empty_concept_error)
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}
