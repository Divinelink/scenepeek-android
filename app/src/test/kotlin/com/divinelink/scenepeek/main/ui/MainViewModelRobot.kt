package com.divinelink.scenepeek.main.ui

import android.net.Uri
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.TestFindByIdUseCase
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
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
  private val findByIdUseCase = TestFindByIdUseCase()

  fun buildViewModel() = apply {
    viewModel = MainViewModel(
      themedActivityDelegate = themedActivityDelegate,
      createSessionUseCase = createSessionUseCase.mock,
      findByIdUseCase = findByIdUseCase.mock,
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

  fun mockFindById(result: Result<MediaItem>) = apply {
    findByIdUseCase.mockSuccess(result)
  }

  suspend fun verifySessionInvoked() = apply {
    createSessionUseCase.verifySessionInvoked()
  }

  fun verifyNoSessionInteraction() = apply {
    createSessionUseCase.verifyNoSessionInteraction()
  }
}
