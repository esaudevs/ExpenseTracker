package com.esaudev.expensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.esaudev.expensetracker.ui.features.loading.navigation.loadingScreen
import com.esaudev.expensetracker.ui.features.onboarding.navigation.onboardingRoute
import com.esaudev.expensetracker.ui.features.onboarding.navigation.onboardingScreen
import com.esaudev.expensetracker.ui.features.tracker.navigation.navigateToTracker
import com.esaudev.expensetracker.ui.features.tracker.navigation.trackerScreen

@Composable
fun ExpenseTrackerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = onboardingRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        onboardingScreen(
            onComplete = {
                navController.navigateToTracker()
            }
        )

        trackerScreen()
        loadingScreen()
    }
}
