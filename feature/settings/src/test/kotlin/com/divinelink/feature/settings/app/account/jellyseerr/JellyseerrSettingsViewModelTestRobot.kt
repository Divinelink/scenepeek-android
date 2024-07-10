package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class JellyseerrSettingsViewModelTestRobot : ViewModelTestRobot<JellyseerrSettingsUiState>() {

  private val logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
  private val loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
  private val getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  private lateinit var viewModel: JellyseerrSettingsViewModel

  override fun buildViewModel() = apply {
    viewModel = JellyseerrSettingsViewModel(
      logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
      getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
      loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
    )
  }

  fun mockLoginJellyseerrResponse(response: Result<JellyseerrAccountDetails>) = apply {
    loginJellyseerrUseCase.mockSuccess(flowOf(response))
  }

  fun mockJellyseerrAccountDetailsResponse(response: Result<JellyseerrAccountDetails?>) = apply {
    getJellyseerrDetailsUseCase.mockSuccess(response)
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

  fun onSelectedJellyseerrLoginMethod(method: JellyseerrLoginMethod) = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnSelectLoginMethod(method))
  }

  fun onLoginJellyseerr() = apply {
    viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnLoginClick)
  }

  fun onDismissSnackbar() = apply {
    viewModel.dismissSnackbar()
  }

  override val actualUiState: Flow<JellyseerrSettingsUiState>
    get() = viewModel.uiState
}
