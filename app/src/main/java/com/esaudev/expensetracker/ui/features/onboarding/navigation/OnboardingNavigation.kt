package com.esaudev.expensetracker.ui.features.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.esaudev.expensetracker.ui.features.onboarding.OnboardingRoute

const val onboardingRoute = "onboarding_route"

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(
        route = onboardingRoute,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.onboardingScreen(
    onComplete: () -> Unit
) {
    composable(
        route = onboardingRoute
    ) {
        OnboardingRoute(
            onComplete = onComplete
        )
    }
}
