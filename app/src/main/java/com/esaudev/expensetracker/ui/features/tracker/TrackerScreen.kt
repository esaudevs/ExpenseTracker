package com.esaudev.expensetracker.ui.features.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.ui.components.ExpenseItem
import com.esaudev.expensetracker.ui.components.MonthSelector
import com.esaudev.expensetracker.ui.components.MonthlyExpenses
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
                TrackerLoading()
            }

            is TrackerViewModel.TrackerUiState.WithContent -> {
                TrackerContent(
                    modifier = Modifier.padding(paddingValues),
                    userName = uiState.userName,
                    monthlyExpenses = uiState.monthlyExpenses,
                    expenses = uiState.expenses,
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
    userName: String,
    monthlyExpenses: String,
    expenses: List<Expense>,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TrackerHeader(
            userName = userName,
            monthlyExpenses = monthlyExpenses
        )

        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(expenses, key = { _, item ->
                item.id
            }) { index, expense ->
                ExpenseItem(concept = expense.concept, amount = expense.amount)

                if (index != expenses.lastIndex) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun TrackerHeader(
    userName: String,
    monthlyExpenses: String
) {
    Column(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 6.dp
        )
    ) {
        Text(text = stringResource(id = R.string.tracker__welcome_message, userName))

        Spacer(modifier = Modifier.height(2.dp))

        MonthSelector(
            modifier = Modifier.fillMaxWidth(),
            date = LocalDateTime.of(2024, 1, 1, 0, 0),
            onPreviousMonth = {
            },
            onNextMonth = {
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        MonthlyExpenses(amount = monthlyExpenses)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.tracker__expenses_title),
            style = MaterialTheme.typography.titleMedium
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
