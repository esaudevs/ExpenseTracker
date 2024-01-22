package com.esaudev.expensetracker.ui.features.tracker.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.esaudev.expensetracker.ui.features.tracker.TrackerRoute

const val trackerRoute = "tracker_route"

fun NavController.navigateToTracker(navOptions: NavOptions? = null) {
    navigate(
        route = trackerRoute,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.trackerScreen() {
    composable(
        route = trackerRoute
    ) {
        TrackerRoute()
    }
}
