package com.divinelink.scenepeek.main.ui

import android.net.Uri
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
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
          DetailsRoute(
            id = 693134,
            mediaType = MediaType.MOVIE,
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
          DetailsRoute(
            id = 693134,
            mediaType = MediaType.TV,
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
          PersonRoute(
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

  @Test
  fun `test handleDeepLink with imdb deeplink with title`() = runTest {
    val url = Uri.parse("https://www.imdb.com/title/tt0044741/")

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItemFactory.FightClub()))
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsRoute(
            id = 1123,
            mediaType = MediaType.MOVIE,
            isFavorite = false,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink for tv`() = runTest {
    val url = Uri.parse("https://www.imdb.com/title/tt11280740")

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItemFactory.theOffice()))
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsRoute(
            id = 2316,
            mediaType = MediaType.TV,
            isFavorite = false,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink with name`() = runTest {
    val url = Uri.parse("https://www.imdb.com/name/nm0044741/")

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItemFactory.Person()))
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToPersonDetails(
          PersonRoute(
            id = 1215572,
            name = "Randall Einhorn",
            profilePath = null,
            knownForDepartment = "Directing",
            gender = Gender.MALE,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink and unknown media type`() = runTest {
    val url = Uri.parse("https://www.imdb.com/name/nm0044741/")

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItem.Unknown))
      .onHandleDeeplink(url)
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with imdb and findById failure does not emit anything`() = runTest {
    val url = Uri.parse("https://www.imdb.com/name/nm0044741/")

    robot
      .buildViewModel()
      .onHandleDeeplink(url)
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
