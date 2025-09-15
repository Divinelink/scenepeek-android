package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import com.divinelink.core.commons.domain.data
import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsResultFactory
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.ui.TestTags
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class JellyseerrSettingsScreenTest : ComposeTest() {

  private lateinit var logoutJellyseerrUseCase: FakeLogoutJellyseerrUseCase
  private lateinit var loginJellyseerrUseCase: FakeLoginJellyseerrUseCase
  private lateinit var getJellyseerrDetailsUseCase: FakeGetJellyseerrDetailsUseCase

  private val loggedInJellyseerr = Result.success(
    JellyseerrAccountDetailsResultFactory.jellyseerr(),
  )
  private val loggedInJellyfin = Result.success(
    JellyseerrAccountDetailsResultFactory.jellyfin(),
  )

  @Before
  fun setUp() {
    logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
    loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
    getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
  }

  @Test
  fun `test update jellyseerr state to logged in iff user is logged in`() = runTest {
    getJellyseerrDetailsUseCase.mockSuccess(loggedInJellyseerr)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = true,
      )
    }

    assertThat(viewModel.uiState.value.jellyseerrState).isEqualTo(
      JellyseerrState.LoggedIn(
        accountDetails = loggedInJellyseerr.data.profile!!,
        address = loggedInJellyseerr.data.address,
        isLoading = false,
      ),
    )
  }

  @Test
  fun `test jellyseerr state is initial when user is not logged in`() = runTest {
    getJellyseerrDetailsUseCase.mockSuccess(
      Result.success(
        JellyseerrProfileResult(
          address = "",
          profile = null,
        ),
      ),
    )
    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = true,
      )
    }

    assertThat(viewModel.uiState.value.jellyseerrState).isEqualTo(
      JellyseerrState.Login(isLoading = false),
    )
  }

  @Test
  fun `test login with jellyfin account`() = runTest {
    val channel: Channel<Result<JellyseerrProfileResult>> = Channel()
    loginJellyseerrUseCase.mockSuccess(flowOf(Result.success(Unit)))
    getJellyseerrDetailsUseCase.mockSuccess(channel)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = false,
      )
    }

    with(composeTestRule) {
      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithText("Jellyfin").assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(loggedInJellyfin.getOrNull()?.profile?.displayName!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.profile?.displayName!!))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON).performClick()

      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.jellyfin()))

      // Success login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT).assertIsDisplayed()

      val loggedInUsername = JellyseerrProfileFactory.jellyfin().displayName
      onNodeWithText(loggedInUsername).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with jellyseerr account`() = runTest {
    val channel: Channel<Result<JellyseerrProfileResult>> = Channel()
    loginJellyseerrUseCase.mockSuccess(flowOf(Result.success(Unit)))
    getJellyseerrDetailsUseCase.mockSuccess(channel)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = false,
      )
    }

    with(composeTestRule) {
      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithText("Jellyseerr").assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(loggedInJellyfin.getOrNull()?.profile?.displayName!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.profile?.displayName!!))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON).performClick()

      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()))

      // Success login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT).assertIsDisplayed()

      val loggedInUsername = JellyseerrProfileFactory.jellyseerr().displayName
      onNodeWithText(loggedInUsername).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with emby account`() = runTest {
    val channel: Channel<Result<JellyseerrProfileResult>> = Channel()
    loginJellyseerrUseCase.mockSuccess(flowOf(Result.success(Unit)))
    getJellyseerrDetailsUseCase.mockSuccess(channel)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = false,
      )
    }

    with(composeTestRule) {
      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithText("Emby").assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(loggedInJellyfin.getOrNull()?.profile?.displayName!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.profile?.displayName!!))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON).performClick()

      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.jellyfin()))

      // Success login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT).assertIsDisplayed()

      val loggedInUsername = JellyseerrProfileFactory.jellyfin().displayName
      onNodeWithText(loggedInUsername).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON).assertIsDisplayed()
    }
  }

  @Test
  fun `test logout jellyseerr account when user is logged in`() = runTest {
    val channel: Channel<Result<JellyseerrProfileResult>> = Channel()
    logoutJellyseerrUseCase.mockSuccess(Result.success(Unit))
    getJellyseerrDetailsUseCase.mockSuccess(channel)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = true,
      )
    }

    with(composeTestRule) {
      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()))
      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsNotDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGOUT_BUTTON)
        .assertIsDisplayed()
        .performClick()

      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test jellyseerr settings withNavigationBar false does not show nav bar`() = runTest {
    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = false,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Components.NAVIGATION_BAR).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test jellyseerr settings withNavigationBar true shows nav bar`() = runTest {
    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = true,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Components.NAVIGATION_BAR).assertIsDisplayed()
    }
  }

  @Test
  fun `test login with error shows basic alert dialog with share error option`() = runTest {
    val channel: Channel<Result<JellyseerrProfileResult>> = Channel()
    loginJellyseerrUseCase.mockSuccess(
      flowOf(Result.failure(Exception("Failed to login to your jellyseerr instance."))),
    )
    getJellyseerrDetailsUseCase.mockSuccess(channel)

    val viewModel = setupViewModel()

    setVisibilityScopeContent {
      JellyseerrSettingsScreen(
        viewModel = viewModel,
        sharedTransitionScope = it,
        onNavigateUp = {},
        withNavigationBar = false,
      )
    }

    with(composeTestRule) {
      channel.send(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()

      waitUntil {
        onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD).isDisplayed()
      }

      onNodeWithTag(TestTags.Settings.Jellyseerr.ADDRESS_TEXT_FIELD)
        .assertIsDisplayed()
        .performClick()
        .performTextInput("http://localhost:8080")

      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithText("Emby").assertIsDisplayed().performClick()

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON,
      ).assertIsNotEnabled()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD).assertIsDisplayed()
      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD).assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD)
        .performClick()
        .performTextInput(loggedInJellyfin.getOrNull()?.profile?.displayName!!)

      onNodeWithTag(TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD)
        .performClick()
        .performTextInput("password")

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.USERNAME_TEXT_FIELD,
      ).assert(hasText(loggedInJellyfin.getOrNull()?.profile?.displayName!!))

      onNodeWithTag(
        TestTags.Settings.Jellyseerr.PASSWORD_TEXT_FIELD,
      ).assert(hasText("••••••••"))

      // Need to scroll to login button to make it visible on the screen because
      // there's not enough space to display it.
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).performScrollToNode(
        hasTestTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON),
      )

      onNodeWithTag(TestTags.Settings.Jellyseerr.JELLYSEERR_LOGIN_BUTTON).performClick()

      // Failed to login
      onNodeWithTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT).assertIsDisplayed()
      onNodeWithTag(TestTags.Dialogs.SHARE_ERROR_BUTTON).assertIsDisplayed()
      onNodeWithText("Failed to login to your jellyseerr instance.").assertIsDisplayed()
    }
  }

  private fun setupViewModel(): JellyseerrSettingsViewModel = JellyseerrSettingsViewModel(
    logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
    getJellyseerrProfileUseCase = getJellyseerrDetailsUseCase.mock,
    loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
  )
}
