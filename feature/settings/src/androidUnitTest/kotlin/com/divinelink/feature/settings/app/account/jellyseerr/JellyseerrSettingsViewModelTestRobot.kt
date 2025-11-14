package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class JellyseerrSettingsViewModelTestRobot : ViewModelTestRobot<JellyseerrSettingsUiState>() {

  private val logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
  private val loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
  private val getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  private lateinit var viewModel: JellyseerrSettingsViewModel

  private val accountDetailsChannel: Channel<Result<JellyseerrProfileResult>> = Channel()

  override fun buildViewModel() = apply {
    getJellyseerrDetailsUseCase.mockSuccess(accountDetailsChannel)

    viewModel = JellyseerrSettingsViewModel(
      logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrDetailsUseCase.mock,
      loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
    )
  }

  fun mockLoginJellyseerrResponse(response: Result<Unit>) = apply {
    loginJellyseerrUseCase.mockSuccess(flowOf(response))
  }

  suspend fun mockLogoutJellyseerrResponse(response: Result<Unit>) = apply {
    logoutJellyseerrUseCase.mockSuccess(response)
  }

  suspend fun verifyLogoutInteraction() = apply {
    logoutJellyseerrUseCase.verifyLogoutInteraction()
  }

  suspend fun mockJellyseerrAccountDetailsResponse(
    response: Result<JellyseerrProfileResult>,
  ) = apply {
    accountDetailsChannel.send(response)
  }

  fun onUserAddressChange(address: String) = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnAddressChange(address))
  }

  fun onUsernameChange(username: String) = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnUsernameChange(username))
  }

  fun onPasswordChange(password: String) = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnPasswordChange(password))
  }

  fun onSelectedJellyseerrLoginMethod(method: JellyseerrAuthMethod) = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnSelectLoginMethod(method))
  }

  suspend fun onLoginJellyseerr(result: Result<JellyseerrProfileResult>? = null) = apply {
    result?.let {
      accountDetailsChannel.send(result)
    }
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnLoginClick)
  }

  suspend fun onLogoutJellyseerr(accountDetailsResult: Result<JellyseerrProfileResult>) =
    apply {
      accountDetailsChannel.send(accountDetailsResult)
      viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnLogoutClick)
    }

  fun onDismissSnackbar() = apply {
    viewModel.dismissSnackbar()
  }

  fun onDismissDialog() = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnDismissDialog)
  }

  override val actualUiState: Flow<JellyseerrSettingsUiState>
    get() = viewModel.uiState
}
