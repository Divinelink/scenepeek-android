package com.andreolas.movierama.main.ui

import com.andreolas.movierama.MainUiState
import com.andreolas.movierama.MainViewModel
import com.andreolas.movierama.fakes.usecase.FakeSetRemoteConfigUseCase
import com.andreolas.movierama.test.util.fakes.FakeThemedActivityDelegate
import com.divinelink.core.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class MainViewModelRobot {
  private lateinit var viewModel: MainViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeSetRemoteConfigUseCase = FakeSetRemoteConfigUseCase()
  private val fakeThemedActivityDelegate = FakeThemedActivityDelegate()

  fun buildViewModel() = apply {
    viewModel = MainViewModel(
      themedActivityDelegate = fakeThemedActivityDelegate,
    )
  }

  fun assertViewState(expectedViewState: MainUiState) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun mockSetRemoteConfigResult(result: Unit) = apply {
    fakeSetRemoteConfigUseCase.mockSetRemoteConfigResult(result)
  }
}
