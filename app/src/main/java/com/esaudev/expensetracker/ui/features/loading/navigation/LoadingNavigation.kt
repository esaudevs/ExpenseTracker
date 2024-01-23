package com.esaudev.expensetracker.ui.features.loading.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val loadingRoute = "loading_route"

fun NavController.navigateToLoading(navOptions: NavOptions? = null) {
    navigate(
        route = loadingRoute,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.loadingScreen() {
    composable(
        route = loadingRoute
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
        }
    }
}
