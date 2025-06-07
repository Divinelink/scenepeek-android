package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.commons.exception.ApiClientException
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.Password
import com.divinelink.core.model.UIText
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class JellyseerrSettingsViewModelTest {

  private val testRobot = JellyseerrSettingsViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test on 401 when jellyseerr expect invalid credentials error`() = runTest {
    testRobot.mockLoginJellyseerrResponse(
      Result.failure(InvalidStatusException(401)),
    )

    testRobot
      .mockLoginJellyseerrResponse(Result.failure(InvalidStatusException(401)))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_invalid_credentials),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
  }

  @Test
  fun `test on UnknownHostException when jellyseerr expect could not connect error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(UnknownHostException()))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_could_not_connect),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
  }

  @Test
  fun `test on ConnectException when jellyseerr expect could not connect error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(ConnectException()))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_could_not_connect),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
  }

  @Test
  fun `test on 500 when jellyseerr expect unknown error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(InvalidStatusException(500)))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText(InvalidStatusException(500).message!!),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
  }

  @Test
  fun `test logout with UnauthorizedException returns initial state`() = runTest {
    testRobot
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
      )
      .mockLogoutJellyseerrResponse(Result.failure(InvalidStatusException(401)))
      .buildViewModel()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            isLoading = false,
          ),
        ),
      )
      .onLogoutJellyseerr()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            address = "",
          ),
        ),
      )
  }

  @Test
  fun `test logout with error shows snackbar`() = runTest {
    testRobot
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
      )
      .mockLogoutJellyseerrResponse(Result.failure(InvalidStatusException(500)))
      .buildViewModel()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            isLoading = false,
          ),
        ),
      )
      .onLogoutJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText(InvalidStatusException(500).message!!),
          ),
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            isLoading = false,
          ),
        ),
      )
  }

  @Test
  fun `test logout with error and no message shows generic error`() = runTest {
    testRobot
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsFactory.jellyseerr()),
      )
      .mockLogoutJellyseerrResponse(
        Result.failure(
          ApiClientException(
            message = null,
            cause = null,
          ),
        ),
      )
      .buildViewModel()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            isLoading = false,
          ),
        ),
      )
      .onLogoutJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(uiR.string.core_ui_error_retry),
          ),
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            isLoading = false,
          ),
        ),
      )
  }

  @Test
  fun `test dismissSnackbar removes snackbar`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(InvalidStatusException(500)))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText(InvalidStatusException(500).message!!),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
      .onDismissSnackbar()
      .assertUiState(
        createUiState(
          snackbarMessage = null,
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            address = "http://localhost:8096",
            jellyseerrLogin = JellyseerrLoginData(
              address = "",
              username = Username("username"),
              password = Password("password"),
            ),
            jellyfinLogin = JellyseerrLoginData.empty(),
          ),
        ),
      )
  }

  @Test
  fun `test getJellyseerrAccount with null sets logged in data`() = runTest {
    testRobot
      .mockJellyseerrAccountDetailsResponse(Result.success(null))
      .buildViewModel()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            address = "",
          ),
        ),
      )
  }

  @Test
  fun `test login and then logout`() = runTest {
    testRobot
      .mockJellyseerrAccountDetailsResponse(Result.success(null))
      .buildViewModel()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            address = "",
          ),
        ),
      )
      .mockLoginJellyseerrResponse(Result.success(JellyseerrAccountDetailsFactory.jellyseerr()))
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .expectUiStates(
        action = { onLoginJellyseerr() },
        uiStates = listOf(
          createUiState(
            jellyseerrState = JellyseerrState.Initial(
              address = "",
              isLoading = false,
              preferredOption = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
          createUiState(
            jellyseerrState = JellyseerrState.LoggedIn(
              isLoading = false,
              accountDetails = JellyseerrAccountDetailsFactory.jellyseerr(),
            ),
          ),
        ),
      )
      .mockLogoutJellyseerrResponse(Result.success(""))
      .onLogoutJellyseerr()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Initial(
            address = "",
            isLoading = false,
            preferredOption = null,
          ),
        ),
      )
  }

  private fun createUiState(
    snackbarMessage: SnackbarMessage? = null,
    jellyseerrState: JellyseerrState = JellyseerrState.Initial(
      address = "",
      isLoading = false,
      preferredOption = null,
    ),
  ) = JellyseerrSettingsUiState(
    snackbarMessage = snackbarMessage,
    jellyseerrState = jellyseerrState,
  )
}
