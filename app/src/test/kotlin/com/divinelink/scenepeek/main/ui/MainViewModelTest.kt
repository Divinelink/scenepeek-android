package com.divinelink.scenepeek.main.ui

import android.net.Uri
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.scenepeek.MainUiEvent
import com.divinelink.scenepeek.MainUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
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
    val url = Uri.parse("https://www.themoviedb.org/movie/693134-dune-part-two")

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
    val url = Uri.parse("https://www.themoviedb.org/tv/693134-dune-part-two")

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
    val url = Uri.parse("https://www.themoviedb.org/person/693134-dune-part-two")

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
    val url = Uri.parse("https://www.themoviedb.org/invalid/693134-dune-part-two")

    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(url)
      .assertUiEvent(MainUiEvent.None)
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeeplink with null deeplink`() {
    robot
      .buildViewModel()
      .assertUiEvent(MainUiEvent.None)
      .onHandleDeeplink(null)
      .assertUiEvent(MainUiEvent.None)
      .onConsumeUiEvent()
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with tmdb deeplink with approved redirect`() = runTest {
    val url = Uri.parse("scenepeek://auth/redirect?request_token=requestToken&approved=true")

    robot
      .buildViewModel()
      .onHandleDeeplink(url)
      .verifySessionInvoked("requestToken")
  }

  @Test
  fun `test handleDeepLink with tmdb deeplink with denied redirect`() = runTest {
    val url = Uri.parse("scenepeek://auth/redirect?request_token=requestToken&denied=true")

    robot
      .buildViewModel()
      .onHandleDeeplink(url)
      .verifyNoSessionInteraction()
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
