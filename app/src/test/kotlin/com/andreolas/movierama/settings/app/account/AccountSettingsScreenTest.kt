package com.andreolas.movierama.settings.app.account

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.factories.session.model.AccountDetailsFactory
import com.andreolas.factories.session.model.RequestTokenFactory
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.R
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.session.login.FakeCreateRequestTokenUseCase
import com.andreolas.movierama.fakes.usecase.session.login.FakeLogoutUseCase
import com.andreolas.movierama.fakes.usecase.session.login.FakeObserveSessionUseCase
import com.andreolas.movierama.fakes.usecase.settings.app.account.FakeGetAccountDetailsUseCase
import com.andreolas.movierama.session.login.ui.LoginScreenArgs
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.ui.TestTags
import com.divinelink.ui.screens.destinations.LoginWebViewScreenDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountSettingsScreenTest : ComposeTest() {

  private lateinit var destinationsNavigator: FakeDestinationsNavigator
  private lateinit var createRequestTokenUseCase: FakeCreateRequestTokenUseCase
  private lateinit var observeSessionUseCase: FakeObserveSessionUseCase
  private lateinit var getAccountDetailsUseCase: FakeGetAccountDetailsUseCase
  private lateinit var logoutUseCase: FakeLogoutUseCase

  private val requestTokenResult = Result.success(RequestTokenFactory.full().token)
  private val sessionResult = Result.success(true)
  private val accountDetailsResult = Result.success(AccountDetailsFactory.Pinkman())

  private val tags = TestTags.Settings.Account

  @Before
  fun setUp() {
    destinationsNavigator = FakeDestinationsNavigator()

    createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
    observeSessionUseCase = FakeObserveSessionUseCase()
    getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
    logoutUseCase = FakeLogoutUseCase()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @OptIn(ExperimentalCoroutinesApi::class)
  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test login is visible when user not connected`() = runTest {
    observeSessionUseCase.mockFailure()

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGIN_BUTTON).assertExists()
    }
  }

  @Test
  fun `test navigate to login web view when login clicked and success token`() = runTest {
    observeSessionUseCase.mockFailure()
    createRequestTokenUseCase.mockSuccess(requestTokenResult)

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGIN_BUTTON).assertIsDisplayed().performClick()

      runOnIdle {
        destinationsNavigator.verifyNavigatedToDirection(
          LoginWebViewScreenDestination(
            LoginScreenArgs(requestTokenResult.getOrThrow())
          )
        )
      }
    }
  }

  @Test
  fun `test account details are visible when user connected`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    val loggedInText = composeTestRule.activity.getString(
      R.string.AccountSettingsScreen__logged_in_as,
      accountDetailsResult.getOrThrow().username
    )

    with(composeTestRule) {
      onNodeWithText(loggedInText).assertIsDisplayed()
      onNodeWithTag(tags.LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout dialog is shown when logout clicked`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()
    }
  }

  @Test
  fun `test user logs out when confirm button pressed and success logout response`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    logoutUseCase.mockSuccess(Result.success(Unit))

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    val loggedInText = composeTestRule.activity.getString(
      R.string.AccountSettingsScreen__logged_in_as,
      accountDetailsResult.getOrThrow().username
    )

    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.CONFIRM_BUTTON).performClick()

      onNodeWithText(loggedInText).assertDoesNotExist()
      onNodeWithTag(tags.LOGIN_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test dismiss dialog when cancel button pressed`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    composeTestRule.setContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.DISMISS_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertDoesNotExist()
    }
  }

  private fun setupViewModel(): AccountSettingsViewModel = AccountSettingsViewModel(
    createRequestTokenUseCase = createRequestTokenUseCase.mock,
    observeSessionUseCase = observeSessionUseCase.mock,
    getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    logoutUseCase = logoutUseCase.mock
  )
}
