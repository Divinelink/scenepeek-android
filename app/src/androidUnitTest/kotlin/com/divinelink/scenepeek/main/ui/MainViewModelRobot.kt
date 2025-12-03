package com.divinelink.scenepeek.main.ui

import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestCreateSessionUseCase
import com.divinelink.core.testing.usecase.TestFindByIdUseCase
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import com.divinelink.scenepeek.MainViewModel
import com.divinelink.scenepeek.test.util.fakes.FakeThemedActivityDelegate
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class MainViewModelRobot {
  private lateinit var viewModel: MainViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val themedActivityDelegate = FakeThemedActivityDelegate()
  private val createSessionUseCase = TestCreateSessionUseCase()
  private val findByIdUseCase = TestFindByIdUseCase()
  private val getJellyseerrProfileUseCase = FakeGetJellyseerrDetailsUseCase()

  fun buildViewModel() = apply {
    viewModel = MainViewModel(
      themedActivityDelegate = themedActivityDelegate,
      createSessionUseCase = createSessionUseCase.mock,
      findByIdUseCase = findByIdUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrProfileUseCase.mock,
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = TestPreferencesRepository(),
      navigationProviders = emptyList(),
    )
  }

  fun onHandleDeeplink(uri: String?) = apply {
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

  fun mockFindById(result: Result<MediaItem>) = apply {
    findByIdUseCase.mockSuccess(result)
  }

  suspend fun verifySessionInvoked() = apply {
    createSessionUseCase.verifySessionInvoked()
  }

  fun mockGetJellyseerrProfile(response: Flow<Result<JellyseerrProfileResult>>) = apply {
    getJellyseerrProfileUseCase.mockSuccess(response)
  }

  fun verifyJellyseerrRefreshSession(numberOfTimes: Int) = apply {
    getJellyseerrProfileUseCase.verifyInteractions(numberOfTimes)
  }
}
