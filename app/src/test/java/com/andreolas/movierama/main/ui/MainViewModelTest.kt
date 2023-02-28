package com.andreolas.movierama.main.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.MainViewState
import com.andreolas.movierama.ui.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val robot = MainViewModelRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successInitTest() {
        robot
            .mockSetRemoteConfigResult(Unit)
            .buildViewModel()
            .assertViewState(
                MainViewState.Completed
            )
    }

    @Test
    fun errorTest() {
        robot
            .buildViewModel()
            .assertViewState(
                MainViewState.Error(
                    UIText.StringText("Something went wrong. Trying again...")
                )
            )
    }

    @Test
    fun retryTest() {
        robot
            .mockSetRemoteConfigResult(Unit)
            .buildViewModel()
            .onRetryFetchRemoteConfig()
            .assertViewState(
                MainViewState.Completed
            )
    }
}
