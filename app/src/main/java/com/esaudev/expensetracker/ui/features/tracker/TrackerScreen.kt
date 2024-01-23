package com.esaudev.expensetracker.ui.features.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ui.components.MonthSelector
import com.esaudev.expensetracker.ui.features.expenses.CreateExpenseDialog
import java.time.LocalDateTime

@Composable
fun TrackerRoute(
    trackerViewModel: TrackerViewModel = hiltViewModel()
) {
    val uiState by trackerViewModel.uiState.collectAsStateWithLifecycle()

    TrackerScreen(uiState = uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    uiState: TrackerViewModel.TrackerUiState
) {
    var showCreateExpenseDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showCreateExpenseDialog) {
        CreateExpenseDialog(onDismiss = { showCreateExpenseDialog = false })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showCreateExpenseDialog = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(
                            id = R.string.tracker__create_expense_icon_content_desc
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is TrackerViewModel.TrackerUiState.Loading -> {
                TrackerLoading(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is TrackerViewModel.TrackerUiState.WithContent -> {
                TrackerContent(
                    modifier = Modifier.padding(paddingValues),
                    uiState = uiState,
                    onNextMonth = {},
                    onPreviousMonth = {}
                )
            }
        }
    }
}

@Composable
fun TrackerContent(
    modifier: Modifier = Modifier,
    uiState: TrackerViewModel.TrackerUiState.WithContent,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(id = R.string.tracker__welcome_message, uiState.userName))

        Spacer(modifier = Modifier.height(2.dp))

        MonthSelector(
            modifier = Modifier.fillMaxWidth(),
            date = LocalDateTime.now(),
            onPreviousMonth = {
            },
            onNextMonth = {
            }
        )
    }
}

@Composable
fun TrackerLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
