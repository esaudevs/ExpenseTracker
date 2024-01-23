package com.esaudev.expensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun ExpenseItem(
    concept: String,
    amount: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = concept)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.tracker__monthly_expenses_amount, amount),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun ExpenseItemPreview() {
    ExpenseTrackerTheme {
        Surface {
            ExpenseItem(
                concept = "Salida al Ajusco",
                amount = "999.99"
            )
        }
    }
}
