package com.andreolas.movierama.settings.app.account

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.factories.session.model.AccountDetailsFactory
import com.andreolas.factories.session.model.RequestTokenFactory
import com.andreolas.movierama.fakes.usecase.session.login.FakeCreateRequestTokenUseCase
import com.andreolas.movierama.fakes.usecase.session.login.FakeLogoutUseCase
import com.andreolas.movierama.fakes.usecase.settings.app.account.FakeGetAccountDetailsUseCase
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setSharedLayoutContent
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeObserveSessionUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.login.LoginScreenArgs
import com.divinelink.feature.settings.screens.destinations.JellyseerrSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.LoginWebViewScreenDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class AccountSettingsScreenTest : ComposeTest() {

  private lateinit var destinationsNavigator: FakeDestinationsNavigator
  private lateinit var createRequestTokenUseCase: FakeCreateRequestTokenUseCase
  private lateinit var observeSessionUseCase: FakeObserveSessionUseCase
  private lateinit var getAccountDetailsUseCase: FakeGetAccountDetailsUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase
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
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
    getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
    logoutUseCase = FakeLogoutUseCase()
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test login is visible when user not connected`() {
    observeSessionUseCase.mockFailure()

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
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

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGIN_BUTTON).assertIsDisplayed().performClick()

      runOnIdle {
        destinationsNavigator.verifyNavigatedToDirection(
          LoginWebViewScreenDestination(
            LoginScreenArgs(requestTokenResult.getOrThrow()),
          ),
        )
      }
    }
  }

  @Test
  fun `test account details are visible when user connected`() {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    val loggedInText = composeTestRule.activity.getString(
      R.string.AccountSettingsScreen__logged_in_as,
      accountDetailsResult.getOrThrow().username,
    )

    with(composeTestRule) {
      onNodeWithText(loggedInText).assertIsDisplayed()
      onNodeWithTag(tags.LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout dialog is shown when logout clicked`() {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
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

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    val loggedInText = composeTestRule.activity.getString(
      R.string.AccountSettingsScreen__logged_in_as,
      accountDetailsResult.getOrThrow().username,
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
  fun `test dismiss dialog when cancel button pressed`() {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.DISMISS_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertDoesNotExist()
    }
  }

  @Test
  fun `test navigate to JellyseerrSettingsScreen`() {
    val viewModel = setupViewModel()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      destinationsNavigator.verifyNavigatedToDirection(JellyseerrSettingsScreenDestination())
    }
  }

  @Test
  fun `test observe jellyseerr account`() = runTest {
    val jellyseerrChannel = Channel<Result<JellyseerrAccountDetails?>>()
    getJellyseerrDetailsUseCase.mockSuccess(jellyseerrChannel)
    val viewModel = setupViewModel()

    val account = JellyseerrAccountDetailsFactory.jellyseerr()

    setSharedLayoutContent {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).assertIsDisplayed()
      onNodeWithText(getString(R.string.feature_settings_logged_in)).assertDoesNotExist()

      jellyseerrChannel.send(Result.success(account))

      onNodeWithText(getString(R.string.feature_settings_logged_in)).assertIsDisplayed()
      onNodeWithText(account.displayName).assertIsDisplayed()

      jellyseerrChannel.send(Result.success(null))

      onNodeWithText(jellyseerrButton).assertIsDisplayed()
      onNodeWithText(getString(R.string.feature_settings_logged_in)).assertDoesNotExist()
    }
  }

  private fun setupViewModel(): AccountSettingsViewModel = AccountSettingsViewModel(
    createRequestTokenUseCase = createRequestTokenUseCase.mock,
    observeSessionUseCase = observeSessionUseCase.mock,
    getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
    logoutUseCase = logoutUseCase.mock,
  )
}
