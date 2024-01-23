package com.esaudev.expensetracker.ui.features.expenses.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ui.components.OutlinedProgressButton
import com.esaudev.expensetracker.ui.components.ProgressButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseOptionsBottomSheet(
    expenseId: String,
    onDismissRequest: () -> Unit,
    onDeleteClick: (String) -> Unit,
    onEditClick: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 24.dp
                )
        ) {
            Text(
                text = stringResource(id = R.string.expense_options__title),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProgressButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = false,
                text = stringResource(id = R.string.expense_options__edit),
                onClick = onEditClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedProgressButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = false,
                text = stringResource(id = R.string.expense_options__delete),
                onClick = {
                    onDeleteClick(expenseId)
                }
            )
        }
    }
}
