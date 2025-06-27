package com.divinelink.feature.profile

import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class ProfileViewModelTestRobot {

  private lateinit var viewModel: ProfileViewModel

  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  fun buildViewModel() = apply {
    viewModel = ProfileViewModel(
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
    )
  }

  fun mockFetchAccountDetails(response: Result<TMDBAccount>) = apply {
    getAccountDetailsUseCase.mockSuccess(response)
  }

  fun assertUiState(expectedUiState: ProfileUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }
}
