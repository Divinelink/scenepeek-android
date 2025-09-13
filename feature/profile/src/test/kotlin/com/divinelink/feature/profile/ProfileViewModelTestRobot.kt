package com.divinelink.feature.profile

import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ProfileViewModelTestRobot {

  private lateinit var viewModel: ProfileViewModel

  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val authRepository = TestAuthRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  fun buildViewModel() = apply {
    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      authRepository = authRepository.mock,
    )
  }

  fun mockFetchAccountDetails(response: Flow<Result<TMDBAccount>>) = apply {
    getAccountDetailsUseCase.mockSuccess(response)
  }

  fun mockJellyseerrEnabled(enabled: Boolean) = apply {
    authRepository.mockJellyseerrEnabled(enabled)
  }

  fun assertUiState(expectedUiState: ProfileUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }
}
