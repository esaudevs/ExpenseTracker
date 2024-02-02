package com.esaudev.expensetracker.ui.features.expenses.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.domain.model.Expense
import com.esaudev.expensetracker.ui.components.MonthCard
import com.esaudev.expensetracker.ui.components.ProgressButton
import com.esaudev.expensetracker.ui.helpers.ObserveAsEvents
import com.esaudev.expensetracker.ui.theme.ExpenseTrackerTheme
import com.esaudev.expensetracker.util.UiText
import com.esaudev.expensetracker.util.UiTopLevelEvent
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertExpenseDialog(
    expense: Expense? = null,
    onDismiss: () -> Unit,
    viewModel: UpsertExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val calendarState = rememberSheetState()

    LaunchedEffect(key1 = expense) {
        if (expense != null) {
            viewModel.initExpense(expense)
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date(
            selectedDate = uiState.paidAt.toLocalDate(),
            onSelectDate = {
                viewModel.onDateChange(LocalDateTime.of(it, LocalTime.MIDNIGHT))
            }
        )
    )

    ObserveAsEvents(flow = viewModel.uiTopLevelEvent) { event ->
        when (event) {
            is UiTopLevelEvent.Success -> {
                viewModel.clearUiState()
                onDismiss()
            }

            else -> Unit
        }
    }

    UpsertExpenseDialog(
        createExpenseUiState = uiState,
        onDismiss = {
            viewModel.clearUiState()
            onDismiss()
        },
        onAmountChange = viewModel::onAmountChange,
        onConceptChange = viewModel::onConceptChange,
        onCreateExpense = viewModel::onCreateExpense,
        onPaidAtClick = {
            calendarState.show()
        }
    )
}

@Composable
fun UpsertExpenseDialog(
    createExpenseUiState: UpsertExpenseViewModel.CreateExpenseUiState,
    onDismiss: () -> Unit,
    onAmountChange: (String) -> Unit,
    onConceptChange: (String) -> Unit,
    onCreateExpense: () -> Unit,
    onPaidAtClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(id = R.string.create_expense__title_label),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            UpsertExpenseDialogContent(
                amountValue = createExpenseUiState.amount,
                conceptValue = createExpenseUiState.concept,
                amountError = createExpenseUiState.amountError,
                conceptError = createExpenseUiState.conceptError,
                onAmountChange = onAmountChange,
                onConceptChange = onConceptChange,
                paidAt = createExpenseUiState.paidAt,
                onPaidAtClick = onPaidAtClick
            )
        },
        confirmButton = {
            ProgressButton(
                isLoading = createExpenseUiState.isLoading,
                text = stringResource(id = R.string.create_expense__create),
                onClick = onCreateExpense
            )
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpsertExpenseDialogContent(
    amountValue: String,
    conceptValue: String,
    paidAt: LocalDateTime,
    amountError: UiText? = null,
    conceptError: UiText? = null,
    onAmountChange: (String) -> Unit,
    onConceptChange: (String) -> Unit,
    onPaidAtClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = amountValue,
            onValueChange = onAmountChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            label = {
                Text(text = stringResource(id = R.string.create_expense__amount_label))
            },
            shape = RoundedCornerShape(50),
            singleLine = true,
            leadingIcon = {
                Text(
                    modifier = Modifier.padding(start = 0.dp),
                    text = "$",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = "MXN",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            isError = amountError != null,
            supportingText = {
                if (amountError != null) {
                    Column {
                        Text(text = amountError.asString())
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        )
        MonthCard(
            modifier = Modifier.fillMaxWidth(),
            date = paidAt,
            onClick = onPaidAtClick
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = conceptValue,
            onValueChange = onConceptChange,
            label = {
                Text(text = stringResource(id = R.string.create_expense__concept_label))
            },
            shape = RoundedCornerShape(50),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    keyboardController?.hide()
                }
            ),
            isError = conceptError != null,
            supportingText = {
                if (conceptError != null) {
                    Text(text = conceptError.asString())
                }
            }
        )
    }
}

@Preview
@Composable
private fun CreateExpenseDialogContentPreview() {
    ExpenseTrackerTheme {
        Surface {
            UpsertExpenseDialogContent(
                amountValue = "",
                conceptValue = "",
                paidAt = LocalDateTime.now(),
                amountError = null,
                conceptError = null,
                onAmountChange = {},
                onConceptChange = {},
                onPaidAtClick = {}
            )
        }
    }
}
