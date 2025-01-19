package com.divinelink.scenepeek.main.ui

import android.net.Uri
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.scenepeek.MainUiEvent
import com.divinelink.scenepeek.MainUiState
import com.divinelink.scenepeek.MainViewModel
import com.divinelink.scenepeek.fakes.usecase.FakeSetRemoteConfigUseCase
import com.divinelink.scenepeek.test.util.fakes.FakeThemedActivityDelegate
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class MainViewModelRobot {
  private lateinit var viewModel: MainViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeSetRemoteConfigUseCase = FakeSetRemoteConfigUseCase()
  private val themedActivityDelegate = FakeThemedActivityDelegate()
  private val createSessionUseCase = TestCreateSessionUseCase()

  fun buildViewModel() = apply {
    viewModel = MainViewModel(
      themedActivityDelegate = themedActivityDelegate,
      createSessionUseCase = createSessionUseCase.mock,
    )
  }

  fun onHandleDeeplink(uri: Uri?) = apply {
    viewModel.handleDeepLink(uri)
  }

  fun onConsumeUiEvent() = apply {
    viewModel.consumeUiEvent()
  }

  fun assertUiState(expectedUiState: MainUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  fun assertUiEvent(expectedUiEvent: MainUiEvent) = apply {
    assertThat(viewModel.uiEvent.value).isEqualTo(expectedUiEvent)
  }

  fun mockSetRemoteConfigResult(result: Unit) = apply {
    fakeSetRemoteConfigUseCase.mockSetRemoteConfigResult(result)
  }

  suspend fun verifySessionInvoked(requestToken: String) = apply {
    createSessionUseCase.verifySessionInvoked(requestToken)
  }

  suspend fun verifyNoSessionInteraction() = apply {
    createSessionUseCase.verifyNoSessionInteraction()
  }
}
