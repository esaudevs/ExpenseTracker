package com.esaudev.expensetracker.ui.features.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TrackerRoute(
    trackerViewModel: TrackerViewModel = hiltViewModel()
) {
    val uiState by trackerViewModel.uiState.collectAsStateWithLifecycle()

    TrackerScreen(uiState = uiState)
}

@Composable
fun TrackerScreen(
    uiState: TrackerViewModel.TrackerUiState
) {
    when (uiState) {
        is TrackerViewModel.TrackerUiState.Loading -> {
            TrackerLoading()
        }
        is TrackerViewModel.TrackerUiState.WithContent -> {
            TrackerContent(uiState = uiState)
        }
    }
}

@Composable
fun TrackerContent(
    uiState: TrackerViewModel.TrackerUiState.WithContent
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = uiState.userName)
    }
}

@Composable
fun TrackerLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
