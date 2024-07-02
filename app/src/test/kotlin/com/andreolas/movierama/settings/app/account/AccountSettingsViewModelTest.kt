package com.andreolas.movierama.settings.app.account

import com.divinelink.core.commons.ApiConstants.HTTP_ERROR_CODE
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginData
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.account.AccountSettingsViewState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException
import com.divinelink.core.ui.R as uiR

class AccountSettingsViewModelTest {

  private val testRobot = AccountSettingsViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test on 401 when jellyseerr expect invalid credentials error`() = runTest {
    testRobot.mockLoginJellyseerrResponse(
      Result.failure(Exception(HTTP_ERROR_CODE + "401")),
    )

    testRobot
      .mockLoginJellyseerrResponse(Result.failure(Exception(HTTP_ERROR_CODE + "401")))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrLoginMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_invalid_credentials),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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
      .onSelectedJellyseerrLoginMethod(JellyseerrLoginMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_could_not_connect),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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
      .onSelectedJellyseerrLoginMethod(JellyseerrLoginMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(R.string.feature_settings_could_not_connect),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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
      .mockLoginJellyseerrResponse(Result.failure(Exception(HTTP_ERROR_CODE + "500")))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrLoginMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(uiR.string.core_ui_error_retry),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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
  fun `test dismissSnackbar removes snackbar`() = runTest {
    testRobot
      .mockLoginJellyseerrResponse(Result.failure(Exception(HTTP_ERROR_CODE + "500")))
      .buildViewModel()
      .onUserAddressChange("http://localhost:8096")
      .onUsernameChange("username")
      .onPasswordChange("password")
      .onSelectedJellyseerrLoginMethod(JellyseerrLoginMethod.JELLYSEERR)
      .onLoginJellyseerr()
      .assertUiState(
        createUiState(
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(uiR.string.core_ui_error_retry),
          ),
          jellyseerrState = JellyseerrState.Initial(
            isLoading = false,
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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
            preferredOption = JellyseerrLoginMethod.JELLYSEERR,
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

  private fun createUiState(
    requestToken: String? = null,
    navigateToWebView: Boolean? = null,
    alertDialogUiState: AlertDialogUiState? = null,
    accountDetails: AccountDetails? = null,
    snackbarMessage: SnackbarMessage? = null,
    jellyseerrState: JellyseerrState = JellyseerrState.Initial(
      isLoading = false,
      preferredOption = null,
    ),
  ) = AccountSettingsViewState(
    requestToken = requestToken,
    navigateToWebView = navigateToWebView,
    alertDialogUiState = alertDialogUiState,
    accountDetails = accountDetails,
    snackbarMessage = snackbarMessage,
    jellyseerrState = jellyseerrState,
  )
}
