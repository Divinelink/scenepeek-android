package com.divinelink.feature.settings.app.account

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setSharedLayoutContent
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.session.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.session.FakeLogoutUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class AccountSettingsScreenTest : ComposeTest() {

  private lateinit var createRequestTokenUseCase: FakeCreateRequestTokenUseCase
  private lateinit var getAccountDetailsUseCase: FakeGetAccountDetailsUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase
  private lateinit var logoutUseCase: FakeLogoutUseCase

  private val sessionResult = Result.success(true)
  private val accountDetailsResult = Result.success(AccountDetailsFactory.Pinkman())

  private val tags = TestTags.Settings.Account

  @Before
  fun setUp() {
    createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
    getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
    logoutUseCase = FakeLogoutUseCase()
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test login is visible when user not connected`() {
    val viewModel = setupViewModel()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(tags.LOGIN_BUTTON).assertExists()
    }
  }

  @Test
  fun `test account details are visible when user connected`() {
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
      )
    }

    val tmdbAccountText = getString(R.string.feature_settings_tmdb_account)
    val usernameText = accountDetailsResult.getOrThrow().username

    with(composeTestRule) {
      onNodeWithText(usernameText).assertIsDisplayed()
      onNodeWithText(tmdbAccountText).assertIsDisplayed()
      onNodeWithTag(tags.LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout dialog is shown when logout clicked`() {
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
      )
    }
    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()
    }
  }

  @Test
  fun `test user logs out when confirm button pressed and success logout response`() = runTest {
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    logoutUseCase.mockSuccess(Result.success(Unit))

    val viewModel = setupViewModel()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
      )
    }

    val tmdbAccountText = getString(R.string.feature_settings_tmdb_account)
    val usernameText = accountDetailsResult.getOrThrow().username

    with(composeTestRule) {
      onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
      onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.CONFIRM_BUTTON).performClick()

      onNodeWithText(usernameText).assertDoesNotExist()
      onNodeWithText(tmdbAccountText).assertDoesNotExist()
      onNodeWithText(getString(R.string.feature_settings_not_logged_in)).assertIsDisplayed()
      onNodeWithText(
        getString(R.string.feature_settings_sign_in_to_access_features),
      ).assertIsDisplayed()
      onNodeWithTag(tags.LOGIN_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test dismiss dialog when cancel button pressed`() {
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
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

    var navigatedToJellyseerrSettingsScreen = false

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {
          navigatedToJellyseerrSettingsScreen = true
        },
      )
    }
    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      assertThat(navigatedToJellyseerrSettingsScreen).isTrue()
    }
  }

  @Test
  fun `test observe jellyseerr account`() = runTest {
    val jellyseerrChannel = Channel<Result<JellyseerrAccountDetails?>>()
    getJellyseerrDetailsUseCase.mockSuccess(jellyseerrChannel)
    val viewModel = setupViewModel()

    val account = JellyseerrAccountDetailsFactory.jellyseerr()

    setSharedLayoutContent { transitionScope, visibilityScope ->
      AccountSettingsScreen(
        sharedTransitionScope = transitionScope,
        animatedVisibilityScope = visibilityScope,
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToTMDBAuth = {},
        onNavigateToJellyseerrSettingsScreen = {},
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
    getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
    logoutUseCase = logoutUseCase.mock,
  )
}
