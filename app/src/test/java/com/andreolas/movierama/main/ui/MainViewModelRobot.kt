package com.andreolas.movierama.main.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.MainViewModel
import com.andreolas.movierama.MainViewState
import com.andreolas.movierama.fakes.usecase.FakeSetRemoteConfigUseCase
import com.andreolas.movierama.test.util.fakes.FakeThemedActivityDelegate
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelRobot {
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeSetRemoteConfigUseCase = FakeSetRemoteConfigUseCase()
    private val fakeThemedActivityDelegate = FakeThemedActivityDelegate()

    fun buildViewModel() = apply {
        viewModel = MainViewModel(
            themedActivityDelegate = fakeThemedActivityDelegate,
            setRemoteConfigUseCase = fakeSetRemoteConfigUseCase,
        )
    }

    fun assertViewState(expectedViewState: MainViewState) = apply {
        assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
    }

    fun mockSetRemoteConfigResult(result: Unit) = apply {
        fakeSetRemoteConfigUseCase.mockSetRemoteConfigResult(result)
    }

    fun onRetryFetchRemoteConfig() = apply {
        viewModel.retryFetchRemoteConfig()
    }
}
