package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.commons.exception.ApiClientException
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsResultFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.UIText
import com.divinelink.core.model.Username
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.resources.Res
import com.divinelink.feature.settings.resources.feature_settings_could_not_connect
import com.divinelink.feature.settings.resources.feature_settings_invalid_credentials
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class JellyseerrSettingsViewModelTest {

  private val testRobot = JellyseerrSettingsViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test on Unauthorised when jellyseerr expect invalid credentials error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(AppException.Unauthorized("401")))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          dialogState = DialogState(
            message = UIText.ResourceText(Res.string.feature_settings_invalid_credentials),
            error = AppException.Unauthorized("401"),
          ),
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test on Offline when jellyseerr expect could not connect error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(AppException.Offline()))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          dialogState = DialogState(
            message = UIText.ResourceText(Res.string.feature_settings_could_not_connect),
            error = AppException.Offline(),
          ),
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test on SocketTimeout when jellyseerr expect could not connect error`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(AppException.SocketTimeout()))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          dialogState = DialogState(
            message = UIText.ResourceText(Res.string.feature_settings_could_not_connect),
            error = AppException.SocketTimeout(),
          ),
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
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
          dialogState = DialogState(
            message = UIText.StringText(InvalidStatusException(500).message!!),
            error = InvalidStatusException(500),
          ),
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test get jellyseerr details with unauthorised logs out user`() = runTest {
    testRobot
      .buildViewModel()
      .mockJellyseerrAccountDetailsResponse(
        Result.success(
          JellyseerrProfileResult(
            profile = JellyseerrProfileFactory.jellyseerr(),
            address = "http://localhost:5055",
          ),
        ),
      ).assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrProfileFactory.jellyseerr(),
            isLoading = false,
            address = "http://localhost:5055",
          ),
        ),
      )
      .mockJellyseerrAccountDetailsResponse(
        Result.failure(AppException.Unauthorized("Unauthorized")),
      )
      .verifyLogoutInteraction()
  }

  @Test
  fun `test logout with UnauthorizedException returns initial state`() = runTest {
    testRobot
      .mockLogoutJellyseerrResponse(Result.failure(AppException.Unauthorized("401")))
      .buildViewModel()
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrProfileFactory.jellyseerr(),
            isLoading = false,
            address = "http://localhost:5055",
          ),
        ),
      )
      .onLogoutJellyseerr(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData.prefilled("http://localhost:5055"),
          ),
        ),
      )
  }

  @Test
  fun `test logout sets to initial state on any error`() = runTest {
    testRobot
      .buildViewModel()
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()),
      )
      .mockLogoutJellyseerrResponse(
        Result.failure(ApiClientException(message = null, cause = null)),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.LoggedIn(
            accountDetails = JellyseerrProfileFactory.jellyseerr(),
            isLoading = false,
            address = "http://localhost:5055",
          ),
        ),
      )
      .onLogoutJellyseerr(
        accountDetailsResult = Result.success(JellyseerrAccountDetailsResultFactory.signedOut()),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData.prefilled("http://localhost:5055"),
          ),
        ),
      )
  }

  @Test
  fun `test dismissDialog removes dialog`() = runTest {
    testRobot
      .buildViewModel()
      .mockLoginJellyseerrResponse(Result.failure(InvalidStatusException(500)))
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
          dialogState = DialogState(
            message = UIText.StringText(InvalidStatusException(500).message ?: ""),
            error = InvalidStatusException(500),
          ),
        ),
      )
      .onDismissDialog()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test dismissSnackbar removes snackbar`() = runTest {
    testRobot
      .buildViewModel()
      .mockLoginJellyseerrResponse(Result.success(Unit))
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .onLoginJellyseerr(
        Result.failure(Exception("Failed to get account details")),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
          snackbarMessage = SnackbarMessage.from(
            UIText.StringText("Failed to get account details"),
          ),
        ),
      )
      .onDismissSnackbar()
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData(
              address = Address.from("http://localhost:8096"),
              username = Username.from("username"),
              password = Password("password"),
              authMethod = JellyseerrAuthMethod.JELLYSEERR,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test getJellyseerrAccount with null sets logged in data`() = runTest {
    testRobot
      .buildViewModel()
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsResultFactory.initial()),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
          ),
        ),
      )
  }

  @Test
  fun `test login and then logout`() = runTest {
    testRobot
      .buildViewModel()
      .mockJellyseerrAccountDetailsResponse(
        Result.success(JellyseerrAccountDetailsResultFactory.initial()),
      )
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
          ),
        ),
      )
      .mockLoginJellyseerrResponse(Result.success(Unit))
      .onSelectedJellyseerrLoginMethod(JellyseerrAuthMethod.JELLYSEERR)
      .expectUiStates(
        action = {
          launch {
            onLoginJellyseerr(Result.success(JellyseerrAccountDetailsResultFactory.jellyseerr()))
          }
        },
        uiStates = listOf(
          createUiState(
            jellyseerrState = JellyseerrState.Login(
              isLoading = false,
              loginData = JellyseerrLoginData.empty().copy(
                authMethod = JellyseerrAuthMethod.JELLYSEERR,
              ),
            ),
          ),
          createUiState(
            jellyseerrState = JellyseerrState.LoggedIn(
              isLoading = false,
              accountDetails = JellyseerrProfileFactory.jellyseerr(),
              address = "http://localhost:5055",
            ),
          ),
        ),
      )
      .mockLogoutJellyseerrResponse(Result.success(Unit))
      .onLogoutJellyseerr(Result.success(JellyseerrAccountDetailsResultFactory.signedOut()))
      .assertUiState(
        createUiState(
          jellyseerrState = JellyseerrState.Login(
            isLoading = false,
            loginData = JellyseerrLoginData.prefilled("http://localhost:5055"),
          ),
        ),
      )
  }

  private fun createUiState(
    snackbarMessage: SnackbarMessage? = null,
    dialogState: DialogState? = null,
    jellyseerrState: JellyseerrState = JellyseerrState.Login(
      isLoading = false,
    ),
  ) = JellyseerrSettingsUiState(
    snackbarMessage = snackbarMessage,
    dialogState = dialogState,
    jellyseerrState = jellyseerrState,
  )
}
