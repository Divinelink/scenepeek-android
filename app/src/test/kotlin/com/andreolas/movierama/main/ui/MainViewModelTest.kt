package com.andreolas.movierama.main.ui

import com.andreolas.movierama.MainUiEvent
import com.andreolas.movierama.MainUiState
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.MainDispatcherRule
import org.junit.Rule
import kotlin.test.Test

class MainViewModelTest {

  private val robot = MainViewModelRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun successInitTest() {
    robot
      .mockSetRemoteConfigResult(Unit)
      .buildViewModel()
      .assertUiState(
        MainUiState.Completed,
      )
  }

  @Test
  fun `test handleDeepLink with movie deeplink`() {
    val url = "https://www.themoviedb.org/movie/693134-dune-part-two"

    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsNavArguments(
            id = 693134,
            mediaType = "movie",
            isFavorite = false,
          ),
        ),
      )
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with tv deeplink`() {
    val url = "https://www.themoviedb.org/tv/693134-dune-part-two"

    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsNavArguments(
            id = 693134,
            mediaType = "tv",
            isFavorite = false,
          ),
        ),
      )
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with person deeplink`() {
    val url = "https://www.themoviedb.org/person/693134-dune-part-two"

    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToPersonDetails(
          PersonNavArguments(
            id = 693134,
            knownForDepartment = null,
            name = null,
            profilePath = null,
            gender = Gender.NOT_SET,
          ),
        ),
      )
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with invalid deeplink`() {
    val url = "https://www.themoviedb.org/invalid/693134-dune-part-two"

    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(url)
      .assertUiEvent(MainUiEvent.None)
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
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
