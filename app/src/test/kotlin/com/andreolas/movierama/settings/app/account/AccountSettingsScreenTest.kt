package com.andreolas.movierama.settings.app.account

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.andreolas.factories.session.model.AccountDetailsFactory
import com.andreolas.factories.session.model.RequestTokenFactory
import com.andreolas.movierama.fakes.usecase.session.login.FakeCreateRequestTokenUseCase
import com.andreolas.movierama.fakes.usecase.session.login.FakeLogoutUseCase
import com.andreolas.movierama.fakes.usecase.settings.app.account.FakeGetAccountDetailsUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountStatusFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeObserveSessionUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.login.LoginScreenArgs
import com.divinelink.feature.settings.screens.destinations.LoginWebViewScreenDestination
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
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
  private lateinit var logoutJellyseerrUseCase: FakeLogoutJellyseerrUseCase
  private lateinit var loginJellyseerrUseCase: FakeLoginJellyseerrUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase

  private val requestTokenResult = Result.success(RequestTokenFactory.full().token)
  private val sessionResult = Result.success(true)
  private val accountDetailsResult = Result.success(AccountDetailsFactory.Pinkman())

  private val loggedInJellyseerr = Result.success(JellyseerrAccountStatusFactory.jellyseerr())
  private val loggedInJellyfin = Result.success(JellyseerrAccountStatusFactory.jellyfin())

  private val tags = TestTags.Settings.Account

  @Before
  fun setUp() {
    destinationsNavigator = FakeDestinationsNavigator()

    createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
    observeSessionUseCase = FakeObserveSessionUseCase()
    getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
    logoutUseCase = FakeLogoutUseCase()
    logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
    loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun `test login is visible when user not connected`() = runTest {
    observeSessionUseCase.mockFailure()

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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
  fun `test account details are visible when user connected`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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
  fun `test logout dialog is shown when logout clicked`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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
  fun `test dismiss dialog when cancel button pressed`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
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
  fun `test update jellyseerr state to logged in iff user is logged in`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
    getJellyseerrDetailsUseCase.mockSuccess(loggedInJellyseerr)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    assertThat(viewModel.viewState.value.jellyseerrState).isEqualTo(
      JellyseerrState.LoggedIn(
        loginData = loggedInJellyseerr.data,
        isLoading = false,
      ),
    )
  }

  @Test
  fun `test jellyseerr state is initial when user is not logged in`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    assertThat(viewModel.viewState.value.jellyseerrState).isEqualTo(
      JellyseerrState.Initial(isLoading = false),
    )
  }

  @Test
  fun `test onLoginClick when user is logged out opens up bottom sheet`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with jellyfin account`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyfin))

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      val jellyfinText = getString(R.string.feature_settings_login_using_jellyfin)

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithText(jellyfinText).assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD).isDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(loggedInJellyfin.getOrNull()?.username!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.username!!))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON).performClick()

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON)
        .performScrollTo()
        .assertIsDisplayed()
        .performClick()

      // Success login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsDisplayed()

      val loggedInText = "You are logged in using your Jellyfin " +
        "${JellyseerrAccountStatusFactory.jellyfin().username} account."
      onNodeWithText(loggedInText).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with jellyseerr account`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      val jellyseerrText = getString(R.string.feature_settings_login_using_jellyseerr)

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled().assertIsDisplayed()

      onNodeWithText(jellyseerrText).assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD,
      ).assertIsNotDisplayed()
      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD,
      ).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(JellyseerrAccountStatusFactory.jellyseerr().username)

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD,
      ).assert(hasText(JellyseerrAccountStatusFactory.jellyseerr().username))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON)
        .performScrollTo()
        .assertIsDisplayed()
        .performClick()

      // Success login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsDisplayed()

      val loggedInText = "You are logged in using your Jellyseerr " +
        "${JellyseerrAccountStatusFactory.jellyseerr().username} account."
      onNodeWithText(loggedInText).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout jellyseerr account when user is logged in`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
    getJellyseerrDetailsUseCase.mockSuccess(loggedInJellyseerr)
    logoutJellyseerrUseCase.mockSuccess(flowOf(Result.success(Unit)))

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON)
        .assertIsDisplayed()
        .performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test re-selecting the same login method hides it`() = runTest {
    observeSessionUseCase.mockSuccess(sessionResult)
    getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))

    val viewModel = setupViewModel()

    setContentWithTheme {
      AccountSettingsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel,
      )
    }

    val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

    with(composeTestRule) {
      onNodeWithText(jellyseerrButton).performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      val jellyseerrText = getString(R.string.feature_settings_login_using_jellyseerr)

      onNodeWithText(jellyseerrText).assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_EXPANDABLE_CARD_BUTTON)
        .assertIsDisplayed()
        .performClick()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD)
        .assertIsNotDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
        .assertIsNotDisplayed()
    }
  }

  @Test
  fun `test credentials for login methods are kept across expanding and collapsing cards`() =
    runTest {
      observeSessionUseCase.mockSuccess(sessionResult)
      getAccountDetailsUseCase.mockSuccess(accountDetailsResult)
      loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))

      val viewModel = setupViewModel()

      setContentWithTheme {
        AccountSettingsScreen(
          navigator = destinationsNavigator,
          viewModel = viewModel,
        )
      }

      val jellyseerrButton = getString(R.string.feature_settings_jellyseerr_integration)

      with(composeTestRule) {
        onNodeWithText(jellyseerrButton).performClick()

        onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()

        waitUntil {
          onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
        }

        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
          .assertIsDisplayed()
          .performClick()
          .performTextInput("http://localhost:8080")

        val jellyseerrText = getString(R.string.feature_settings_login_using_jellyseerr)
        val jellyfinText = getString(R.string.feature_settings_login_using_jellyfin)

        // Open jellyfin expandable card
        onNodeWithText(jellyfinText).assertIsDisplayed().performClick()

        onNodeWithTag(
          TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
        ).assertIsEnabled()

        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD)
          .assertIsDisplayed()
          .performTextInput("Jellyfin credentials")
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD)
          .assertIsDisplayed()
          .performTextInput("Jellyfin password")

        // Hide jellyfin expandable card
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_EXPANDABLE_CARD_BUTTON)
          .assertIsDisplayed()
          .performClick()

        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD)
          .assertIsNotDisplayed()
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
          .assertIsNotDisplayed()

        // Open jellyseerr expandable card

        onNodeWithText(jellyseerrText).assertIsDisplayed().performClick()

        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD)
          .assertIsDisplayed().performTextInput("Jellyseerr credentials")
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
          .assertIsDisplayed().performTextInput("Jellyseerr password")

        // Hide jellyseerr expandable card

        // Show jellyfin again
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_EXPANDABLE_CARD_BUTTON)
          .assertIsDisplayed()
          .performClick()

        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD)
          .assertIsDisplayed()
          .performTextInput("Jellyfin credentials")
        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD)
          .assertIsDisplayed()
          .performTextInput("Jellyfin password")
      }
    }

  private fun setupViewModel(): AccountSettingsViewModel = AccountSettingsViewModel(
    createRequestTokenUseCase = createRequestTokenUseCase.mock,
    observeSessionUseCase = observeSessionUseCase.mock,
    getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    logoutUseCase = logoutUseCase.mock,
    logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
    getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
    loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
  )
}
