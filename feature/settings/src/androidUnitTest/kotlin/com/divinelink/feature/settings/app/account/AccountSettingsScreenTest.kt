package com.divinelink.feature.settings.app.account

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeCreateRequestTokenUseCase
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLogoutUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_jellyseerr_integration
import com.divinelink.feature.settings.feature_settings_logged_in
import com.divinelink.feature.settings.feature_settings_not_logged_in
import com.divinelink.feature.settings.feature_settings_sign_in_to_access_features
import com.divinelink.feature.settings.feature_settings_tmdb_account
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class AccountSettingsScreenTest : ComposeTest() {

  private lateinit var createRequestTokenUseCase: FakeCreateRequestTokenUseCase
  private lateinit var getAccountDetailsUseCase: FakeGetAccountDetailsUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase
  private lateinit var logoutUseCase: FakeLogoutUseCase

  private val accountDetailsResult = Result.success(
    TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
  )

  private val tags = TestTags.Settings.Account

  @Before
  override fun setUp() {
    createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
    getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
    logoutUseCase = FakeLogoutUseCase()
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test login is visible when user not connected`() = uiTest {
    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    onNodeWithTag(tags.LOGIN_BUTTON).assertExists()
  }

  @Test
  fun `test account details are visible when user connected`() = uiTest {
    getAccountDetailsUseCase.mockSuccess(flowOf(accountDetailsResult))

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    val tmdbAccountText = getString(Res.string.feature_settings_tmdb_account)
    val usernameText = accountDetailsResult.getOrThrow().accountDetails.username

    onNodeWithText(usernameText).assertIsDisplayed()
    onNodeWithText(tmdbAccountText).assertIsDisplayed()
    onNodeWithTag(tags.LOGOUT_BUTTON).assertIsDisplayed()
  }

  @Test
  fun `test logout dialog is shown when logout clicked`() = uiTest {
    getAccountDetailsUseCase.mockSuccess(flowOf(accountDetailsResult))

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }
    onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
    onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()
  }

  @Test
  fun `test user logs out when confirm button pressed and success logout response`() = uiTest {
    getAccountDetailsUseCase.mockSuccess(flowOf(accountDetailsResult))

    logoutUseCase.mockSuccess(Result.success(Unit))

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    val tmdbAccountText = getString(Res.string.feature_settings_tmdb_account)
    val usernameText = accountDetailsResult.getOrThrow().accountDetails.username

    onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
    onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.CONFIRM_BUTTON).performClick()

    onNodeWithText(usernameText).assertDoesNotExist()
    onNodeWithText(tmdbAccountText).assertDoesNotExist()
    onNodeWithText(getString(Res.string.feature_settings_not_logged_in)).assertIsDisplayed()
    onNodeWithText(
      getString(Res.string.feature_settings_sign_in_to_access_features),
    ).assertIsDisplayed()
    onNodeWithTag(tags.LOGIN_BUTTON).assertIsDisplayed()
  }

  @Test
  fun `test dismiss dialog when cancel button pressed`() = uiTest {
    getAccountDetailsUseCase.mockSuccess(flowOf(accountDetailsResult))

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    onNodeWithTag(tags.LOGOUT_BUTTON).performClick()
    onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertIsDisplayed()

    onNodeWithTag(TestTags.Dialogs.DISMISS_BUTTON).performClick()
    onNodeWithTag(TestTags.Dialogs.ALERT_DIALOG).assertDoesNotExist()
  }

  @Test
  fun `test navigate to JellyseerrSettingsScreen`() = uiTest {
    val viewModel = setupViewModel()

    var navigatedToJellyseerrSettingsScreen = false

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = { route ->
          if (route is Navigation.JellyseerrSettingsRoute) {
            navigatedToJellyseerrSettingsScreen = true
          }
        },
      )
    }
    val jellyseerrButton = getString(Res.string.feature_settings_jellyseerr_integration)

    onNodeWithText(jellyseerrButton).performClick()

    assertThat(navigatedToJellyseerrSettingsScreen).isTrue()
  }

  @Test
  fun `test observe jellyseerr account`() = uiTest {
    val jellyseerrChannel = Channel<Result<JellyseerrProfileResult>>()
    getJellyseerrDetailsUseCase.mockSuccess(jellyseerrChannel)
    val viewModel = setupViewModel()

    val account = JellyseerrProfileFactory.jellyseerr()

    setVisibilityScopeContent {
      AccountSettingsScreen(
        sharedTransitionScope = it,
        animatedVisibilityScope = this,
        viewModel = viewModel,
        onNavigate = {},
      )
    }
    val jellyseerrButton = getString(Res.string.feature_settings_jellyseerr_integration)

    onNodeWithText(jellyseerrButton).assertIsDisplayed()
    onNodeWithText(getString(Res.string.feature_settings_logged_in)).assertDoesNotExist()

    jellyseerrChannel.send(
      Result.success(
        JellyseerrProfileResult(
          address = "",
          profile = account,
        ),
      ),
    )

    onNodeWithText(getString(Res.string.feature_settings_logged_in)).assertIsDisplayed()
    onNodeWithText(account.displayName).assertIsDisplayed()

    jellyseerrChannel.send(
      Result.success(
        JellyseerrProfileResult(address = "", profile = null),
      ),
    )

    onNodeWithText(jellyseerrButton).assertIsDisplayed()
    onNodeWithText(getString(Res.string.feature_settings_logged_in)).assertDoesNotExist()
  }

  private fun setupViewModel(): AccountSettingsViewModel = AccountSettingsViewModel(
    getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    getJellyseerrProfileUseCase = getJellyseerrDetailsUseCase.mock,
    logoutUseCase = logoutUseCase.mock,
  )
}
