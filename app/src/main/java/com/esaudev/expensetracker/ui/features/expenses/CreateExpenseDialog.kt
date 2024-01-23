package com.esaudev.expensetracker.ui.features.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ui.components.ProgressButton
import com.esaudev.expensetracker.ui.helpers.ObserveAsEvents
import com.esaudev.expensetracker.util.UiTopLevelEvent

@Composable
fun CreateExpenseDialog(
    onDismiss: () -> Unit,
    viewModel: CreateExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.uiTopLevelEvent) { event ->
        when (event) {
            is UiTopLevelEvent.Success -> {
                viewModel.clearUiState()
                onDismiss()
            }
            else -> Unit
        }
    }

    CreateExpenseDialog(
        createExpenseUiState = uiState,
        onDismiss = {
            viewModel.clearUiState()
            onDismiss()
        },
        onAmountChange = viewModel::onAmountChange,
        onConceptChange = viewModel::onConceptChange,
        onCreateExpense = viewModel::onCreateExpense
    )
}

@Composable
fun CreateExpenseDialog(
    createExpenseUiState: CreateExpenseViewModel.CreateExpenseUiState,
    onDismiss: () -> Unit,
    onAmountChange: (String) -> Unit,
    onConceptChange: (String) -> Unit,
    onCreateExpense: () -> Unit
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
            CreateExpenseDialogContent(
                amountValue = createExpenseUiState.amount,
                conceptValue = createExpenseUiState.concept,
                onAmountChange = onAmountChange,
                onConceptChange = onConceptChange
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateExpenseDialogContent(
    amountValue: String,
    conceptValue: String,
    onAmountChange: (String) -> Unit,
    onConceptChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        Text(text = stringResource(id = R.string.create_expense__amount_label))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
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
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = stringResource(id = R.string.create_expense__concept_label))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = conceptValue,
            onValueChange = onConceptChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    keyboardController?.hide()
                }
            )
        )
    }
}
