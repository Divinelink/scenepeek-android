package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import com.divinelink.core.commons.domain.data
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setSharedLayoutContent
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class JellyseerrSettingsScreenTest : ComposeTest() {

  private lateinit var destinationsNavigator: FakeDestinationsNavigator

  private lateinit var logoutJellyseerrUseCase: FakeLogoutJellyseerrUseCase
  private lateinit var loginJellyseerrUseCase: FakeLoginJellyseerrUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase

  private val loggedInJellyseerr = Result.success(JellyseerrAccountDetailsFactory.jellyseerr())
  private val loggedInJellyfin = Result.success(JellyseerrAccountDetailsFactory.jellyfin())

  @Before
  fun setUp() {
    destinationsNavigator = FakeDestinationsNavigator()

    logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
    loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
  }

  @Test
  fun `test update jellyseerr state to logged in iff user is logged in`() = runTest {
    getJellyseerrDetailsUseCase.mockSuccess(loggedInJellyseerr)

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    assertThat(viewModel.uiState.value.jellyseerrState).isEqualTo(
      JellyseerrState.LoggedIn(
        accountDetails = loggedInJellyseerr.data,
        isLoading = false,
      ),
    )
  }

  @Test
  fun `test jellyseerr state is initial when user is not logged in`() = runTest {
    getJellyseerrDetailsUseCase.mockSuccess(Result.success(null))
    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    assertThat(viewModel.uiState.value.jellyseerrState).isEqualTo(
      JellyseerrState.Initial(address = "", isLoading = false),
    )
  }

  @Test
  fun `test login with jellyfin account`() = runTest {
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyfin))
    getJellyseerrDetailsUseCase.mockSuccess(Result.success(null))

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
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
        .performTextInput(loggedInJellyfin.getOrNull()?.displayName!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYFIN_PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYFIN_USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.displayName!!))

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

      val loggedInUsername = JellyseerrAccountDetailsFactory.jellyfin().displayName
      onNodeWithText(loggedInUsername).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with jellyseerr account`() = runTest {
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))
    getJellyseerrDetailsUseCase.mockSuccess(Result.success(null))

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      val jellyseerrText = getString(R.string.feature_settings_login_using_jellyseerr)

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON)
        .performScrollTo()
        .assertIsNotEnabled()
        .assertIsDisplayed()
        .performClick()

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
        .performTextInput(JellyseerrAccountDetailsFactory.jellyseerr().displayName)

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_USERNAME_TEXT_FIELD,
      ).assert(hasText(JellyseerrAccountDetailsFactory.jellyseerr().displayName))

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

      val loggedInUsername = JellyseerrAccountDetailsFactory.jellyseerr().displayName
      onNodeWithText(loggedInUsername).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout jellyseerr account when user is logged in`() = runTest {
    getJellyseerrDetailsUseCase.mockSuccess(loggedInJellyseerr)
    logoutJellyseerrUseCase.mockSuccess(flowOf(Result.success("http://localhost:8080")))

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      runOnUiThread {
        onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsDisplayed()
        onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsNotDisplayed()

        onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON)
          .assertIsDisplayed()
          .performClick()

        onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET).assertIsDisplayed()
        onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET).assertIsNotDisplayed()
      }
    }
  }

  @Test
  fun `test re-selecting the same login method hides it`() = runTest {
    loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))
    getJellyseerrDetailsUseCase.mockSuccess(Result.success(null))

    val viewModel = setupViewModel()

    setSharedLayoutContent {
      JellyseerrSettingsScreen(
        navigator = destinationsNavigator,
        animatedVisibilityScope = it,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
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
      loginJellyseerrUseCase.mockSuccess(flowOf(loggedInJellyseerr))
      getJellyseerrDetailsUseCase.mockSuccess(Result.success(null))

      val viewModel = setupViewModel()

      setSharedLayoutContent {
        JellyseerrSettingsScreen(
          navigator = destinationsNavigator,
          animatedVisibilityScope = it,
          viewModel = viewModel,
        )
      }

      with(composeTestRule) {
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
          .performScrollTo()
          .assertIsDisplayed()
          .performTextInput("Jellyseerr password")

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

  private fun setupViewModel(): JellyseerrSettingsViewModel = JellyseerrSettingsViewModel(
    logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
    getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
    loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
  )
}
