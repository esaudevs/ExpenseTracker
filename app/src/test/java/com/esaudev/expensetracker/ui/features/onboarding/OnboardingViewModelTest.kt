package com.esaudev.expensetracker.ui.features.onboarding

import app.cash.turbine.test
import com.esaudev.expensetracker.domain.usecase.SaveUserNameUseCase
import com.esaudev.expensetracker.domain.usecase.ValidateUserNameUseCase
import com.esaudev.expensetracker.util.MainDispatcherRule
import com.esaudev.expensetracker.util.UiTopLevelEvent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OnboardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userRepository = TestUserRepository()
    private val validateUserNameUseCase = ValidateUserNameUseCase()
    private val saveUserNameUseCase = SaveUserNameUseCase(
        userRepository = userRepository
    )
    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setup() {
        viewModel = OnboardingViewModel(
            validateUserNameUseCase = validateUserNameUseCase,
            saveUserNameUseCase = saveUserNameUseCase
        )
    }

    @Test
    fun `state is initially empty`() = runTest {
        assertEquals(OnboardingViewModel.OnboardingUiState(), viewModel.uiState.value)
    }

    @Test
    fun `on username changed, state is updated`() = runTest {
        viewModel.onNameChanged("Esau")
        assertEquals("Esau", viewModel.uiState.value.name)
    }

    @Test
    fun `on name validation fail, state updated with error`() = runTest {
        viewModel.onNameChanged("")
        viewModel.onContinue()

        viewModel.uiTopLevelEvent.test {
            expectNoEvents()
        }

        assert(viewModel.uiState.value.nameError != null)
    }

    @Test
    fun `on name validation success, success top event is sent`() = runTest {
        viewModel.onNameChanged("Esau")
        viewModel.onContinue()

        viewModel.uiTopLevelEvent.test {
            assertEquals(UiTopLevelEvent.Success, expectMostRecentItem())
        }
        assert(viewModel.uiState.value.nameError == null)
    }

    @Test
    fun `on continue with invalid name, error is set`() = runTest {
        viewModel.uiState.test {
            val initialEmission = awaitItem()
            assertEquals(OnboardingViewModel.OnboardingUiState(), initialEmission)

            viewModel.onNameChanged("#%")
            val nameEmission = awaitItem()
            assertEquals("#%", nameEmission.name)

            viewModel.onContinue()
            val errorEmission = awaitItem()
            assert(errorEmission.nameError != null)
            expectNoEvents()
        }
    }

    @Test
    fun `on continue with valid name, loading is set`() = runTest {
        viewModel.uiState.test {
            val initialEmission = awaitItem()
            assertEquals(OnboardingViewModel.OnboardingUiState(), initialEmission)

            viewModel.onNameChanged("Esau")
            val nameEmission = awaitItem()
            assertEquals("Esau", nameEmission.name)

            viewModel.onContinue()
            val loadingEmission = awaitItem()
            assertEquals(true, loadingEmission.isLoading)

            val hideLoadingEmission = awaitItem()
            assertEquals(false, hideLoadingEmission.isLoading)
            expectNoEvents()
        }
    }
}
