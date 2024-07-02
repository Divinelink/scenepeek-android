package com.andreolas.movierama.settings.app.account

import com.andreolas.movierama.fakes.usecase.session.login.FakeCreateRequestTokenUseCase
import com.andreolas.movierama.fakes.usecase.session.login.FakeLogoutUseCase
import com.andreolas.movierama.fakes.usecase.settings.app.account.FakeGetAccountDetailsUseCase
import com.divinelink.core.model.jellyseerr.JellyseerrAccountStatus
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.FakeLoginJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeLogoutJellyseerrUseCase
import com.divinelink.core.testing.usecase.FakeObserveSessionUseCase
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.app.account.AccountSettingsViewState
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrInteraction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AccountSettingsViewModelTestRobot : ViewModelTestRobot<AccountSettingsViewState>() {

  private val createRequestTokenUseCase = FakeCreateRequestTokenUseCase()
  private val observeSessionUseCase = FakeObserveSessionUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val logoutUseCase = FakeLogoutUseCase()
  private val logoutJellyseerrUseCase = FakeLogoutJellyseerrUseCase()
  private val loginJellyseerrUseCase = FakeLoginJellyseerrUseCase()
  private val getJellyseerrDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  private lateinit var viewModel: AccountSettingsViewModel

  override fun buildViewModel() = apply {
    viewModel = AccountSettingsViewModel(
      createRequestTokenUseCase = createRequestTokenUseCase.mock,
      observeSessionUseCase = observeSessionUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      logoutUseCase = logoutUseCase.mock,
      logoutJellyseerrUseCase = logoutJellyseerrUseCase.mock,
      getJellyseerrDetailsUseCase = getJellyseerrDetailsUseCase.mock,
      loginJellyseerrUseCase = loginJellyseerrUseCase.mock,
    )
  }

  fun mockLoginJellyseerrResponse(response: Result<JellyseerrAccountStatus>) = apply {
    loginJellyseerrUseCase.mockSuccess(flowOf(response))
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

  override val actualUiState: Flow<AccountSettingsViewState>
    get() = viewModel.viewState
}
