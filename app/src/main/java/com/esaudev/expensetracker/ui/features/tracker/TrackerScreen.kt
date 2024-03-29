package com.esaudev.expensetracker.ui.features.tracker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.ext.swappableHorizontally
import com.esaudev.expensetracker.ui.components.ExpenseItem
import com.esaudev.expensetracker.ui.components.MonthSelector
import com.esaudev.expensetracker.ui.components.MonthlyTotal
import com.esaudev.expensetracker.ui.features.expenses.create.UpsertExpenseDialog
import com.esaudev.expensetracker.ui.features.expenses.options.ExpenseOptionsBottomSheet
import com.esaudev.expensetracker.ui.helpers.DialogWithContentState
import java.time.LocalDateTime

@Composable
fun TrackerRoute(
    trackerViewModel: TrackerViewModel = hiltViewModel()
) {
    val uiState by trackerViewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
        trackerViewModel.getExpensesByQuery()
    }

    TrackerScreen(
        uiState = uiState,
        onNextMonth = trackerViewModel::onNextMonth,
        onPreviousMonth = trackerViewModel::onPreviousMonth,
        onDeleteClick = trackerViewModel::onDeleteExpense
    )
}

@Composable
fun TrackerScreen(
    uiState: TrackerUiState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDeleteClick: (Expense) -> Unit
) {
    var upsertExpenseDialogState by remember {
        mutableStateOf(
            DialogWithContentState<Expense>(
                shouldBeVisible = false,
                content = null
            )
        )
    }

    var optionsSheetState by remember {
        mutableStateOf(
            DialogWithContentState<Expense>(
                shouldBeVisible = false,
                content = null
            )
        )
    }

    if (upsertExpenseDialogState.shouldBeVisible) {
        UpsertExpenseDialog(
            expense = upsertExpenseDialogState.content,
            onDismiss = {
                upsertExpenseDialogState = upsertExpenseDialogState.copy(
                    shouldBeVisible = false
                )
            }
        )
    }

    if (optionsSheetState.shouldBeVisible) {
        checkNotNull(optionsSheetState.content)
        ExpenseOptionsBottomSheet(
            expense = optionsSheetState.content!!,
            onDismissRequest = {
                optionsSheetState = optionsSheetState.copy(
                    shouldBeVisible = false
                )
            },
            onEditClick = {
                optionsSheetState = optionsSheetState.copy(
                    shouldBeVisible = false
                )
                upsertExpenseDialogState = upsertExpenseDialogState.copy(
                    shouldBeVisible = true,
                    content = it
                )
            },
            onDeleteClick = {
                optionsSheetState = optionsSheetState.copy(
                    shouldBeVisible = false
                )
                onDeleteClick(it)
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    upsertExpenseDialogState = upsertExpenseDialogState.copy(
                        shouldBeVisible = true,
                        content = null
                    )
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
            is TrackerUiState.Loading -> {
                TrackerLoading()
            }

            is TrackerUiState.WithContent -> {
                TrackerContent(
                    modifier = Modifier.padding(paddingValues),
                    uiState = uiState,
                    onNextMonth = onNextMonth,
                    onPreviousMonth = onPreviousMonth,
                    onExpenseClick = {
                        optionsSheetState = optionsSheetState.copy(
                            shouldBeVisible = true,
                            content = it
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackerContent(
    modifier: Modifier = Modifier,
    uiState: TrackerUiState.WithContent,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onExpenseClick: (Expense) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .swappableHorizontally(
                onSwipeRight = onPreviousMonth,
                onSwipeLeft = onNextMonth
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TrackerHeader(
                userName = uiState.userName,
                monthlyExpenses = uiState.monthlyExpenses,
                date = uiState.date,
                onNextMonth = onNextMonth,
                onPreviousMonth = onPreviousMonth
            )

            if (uiState.expenses.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    itemsIndexed(uiState.expenses, key = { _, item ->
                        item.id
                    }) { index, expense ->
                        ExpenseItem(
                            modifier = Modifier
                                .animateItemPlacement(),
                            expense = expense,
                            onClick = onExpenseClick
                        )

                        if (index != uiState.expenses.lastIndex) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.tracker__empty_expenses),
                        modifier = Modifier.padding(
                            horizontal = 64.dp
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun TrackerHeader(
    userName: String,
    date: LocalDateTime,
    monthlyExpenses: String,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
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
            date = date,
            onPreviousMonth = onPreviousMonth,
            onNextMonth = onNextMonth
        )

        Spacer(modifier = Modifier.height(4.dp))

        MonthlyTotal(amount = monthlyExpenses)

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
