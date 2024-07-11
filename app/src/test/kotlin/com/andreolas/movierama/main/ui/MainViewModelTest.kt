package com.andreolas.movierama.main.ui

import com.andreolas.movierama.MainUiState
import com.divinelink.core.testing.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

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
        MainUiState.Completed,
      )
  }

//  @Test
//  fun errorTest() {
//    robot
//      .buildViewModel()
//      .assertViewState(
//        MainViewState.Error(
//          UIText.StringText("Something went wrong. Trying again...")
//        )
//      )
//  }

//  @Test
//  fun retryTest() {
//    robot
//      .mockSetRemoteConfigResult(Unit)
//      .buildViewModel()
//      .onRetryFetchRemoteConfig()
//      .assertViewState(
//        MainViewState.Completed
//      )
//  }
}
