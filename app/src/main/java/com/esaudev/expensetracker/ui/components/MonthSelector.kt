package com.esaudev.expensetracker.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ext.getMonthNameWithYear
import com.esaudev.expensetracker.ui.theme.ExpenseTrackerTheme
import java.time.LocalDateTime

@Composable
fun MonthSelector(
    date: LocalDateTime,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.previous_month_content_desc)
            )
        }

        AnimatedContent(
            targetState = date,
            label = ""
        ) { targetState ->
            Text(
                text = targetState.getMonthNameWithYear(),
                style = MaterialTheme.typography.titleLarge
            )
        }

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = stringResource(id = R.string.next_month_content_desc)
            )
        }
    }
}

@Preview
@Composable
private fun MonthSelectorPreview() {
    ExpenseTrackerTheme {
        Surface {
            MonthSelector(
                date = LocalDateTime.now(),
                onPreviousMonth = {},
                onNextMonth = {}
            )
        }
    }
}
