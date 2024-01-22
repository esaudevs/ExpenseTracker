package com.esaudev.expensetracker.ui.features.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esaudev.expensetracker.R
import com.esaudev.expensetracker.ui.components.ProgressButton
import com.esaudev.expensetracker.ui.helpers.ObserveAsEvents
import com.esaudev.expensetracker.util.UiText
import com.esaudev.expensetracker.util.UiTopLevelEvent

const val NAME_MAX_LENGTH = 12

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OnboardingRoute(
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val uiState by onboardingViewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(flow = onboardingViewModel.uiTopLevelEvent) { event ->
        when (event) {
            is UiTopLevelEvent.Success -> {
                onComplete()
            }

            else -> Unit
        }
    }

    OnboardingScreen(
        nameValue = uiState.name,
        onNameChange = onboardingViewModel::onNameChanged,
        onContinue = {
            keyboardController?.hide()
            onboardingViewModel.onContinue()
        },
        isLoading = uiState.isLoading,
        nameErrorMessage = uiState.nameError
    )
}

@Composable
fun OnboardingScreen(
    nameValue: String,
    onNameChange: (String) -> Unit,
    onContinue: () -> Unit,
    isLoading: Boolean = false,
    nameErrorMessage: UiText? = null
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {
        OnboardingBodyContent(
            nameValue = nameValue,
            onNameChange = onNameChange,
            onContinue = onContinue,
            nameErrorMessage = nameErrorMessage
        )

        ProgressButton(
            isLoading = isLoading,
            text = stringResource(id = R.string.onboarding__continue),
            onClick = onContinue,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.OnboardingBodyContent(
    nameValue: String,
    onNameChange: (String) -> Unit,
    onContinue: () -> Unit,
    nameErrorMessage: UiText? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
            .padding(horizontal = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.onboarding__how_can_we_call_you),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nameValue,
            onValueChange = onNameChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    onContinue()
                }
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center
            ),
            isError = nameErrorMessage != null,
            supportingText = {
                if (nameErrorMessage != null) {
                    Text(
                        text = nameErrorMessage.asString()
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${nameValue.length}/$NAME_MAX_LENGTH",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
