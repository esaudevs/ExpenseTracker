package com.esaudev.expensetracker.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esaudev.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun ProgressButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = modifier,
            onClick = onClick,
            enabled = !isLoading,
            shape = RoundedCornerShape(50)
        ) {
            if (isLoading) {
                Box {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Transparent
                    )
                }
            } else {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
@Preview(name = "Light mode", showBackground = true)
@Preview(name = "Dark mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ProgressButtonLoadingPreview() {
    ExpenseTrackerTheme {
        Surface {
            ProgressButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = true,
                text = "Button text",
                onClick = {}
            )
        }
    }
}

@Composable
@Preview(name = "Light mode", showBackground = true)
@Preview(name = "Dark mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ProgressButtonNoLoadingPreview() {
    ExpenseTrackerTheme {
        Surface {
            ProgressButton(
                isLoading = false,
                text = "Button text",
                onClick = {}
            )
        }
    }
}
