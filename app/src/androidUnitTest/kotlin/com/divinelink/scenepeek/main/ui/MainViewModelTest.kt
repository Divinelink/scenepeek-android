package com.divinelink.scenepeek.main.ui

import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrProfileFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import kotlinx.coroutines.flow.flowOf
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
          DetailsRoute(
            id = 693134,
            mediaType = MediaType.MOVIE.value,
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
          DetailsRoute(
            id = 693134,
            mediaType = MediaType.TV.value,
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
          PersonRoute(
            id = 693134,
            knownForDepartment = null,
            name = null,
            profilePath = null,
            gender = Gender.NOT_SET.value,
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
    val url = "scenepeek://auth/redirect"

    robot
      .buildViewModel()
      .onHandleDeeplink(url)
      .verifySessionInvoked()
  }

  @Test
  fun `test handleDeepLink with imdb deeplink with title`() = runTest {
    val url = "https://www.imdb.com/title/tt0044741/"

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItemFactory.FightClub()))
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsRoute(
            id = 550,
            mediaType = MediaType.MOVIE.value,
            isFavorite = false,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink for tv`() = runTest {
    val url = "https://www.imdb.com/title/tt11280740"

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItemFactory.theOffice()))
      .onHandleDeeplink(url)
      .assertUiEvent(
        MainUiEvent.NavigateToDetails(
          DetailsRoute(
            id = 2316,
            mediaType = MediaType.TV.value,
            isFavorite = false,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink with name`() = runTest {
    val url = "https://www.imdb.com/name/nm0044741/"

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
            gender = Gender.MALE.value,
          ),
        ),
      )
  }

  @Test
  fun `test handleDeepLink with imdb deeplink and unknown media type`() = runTest {
    val url = "https://www.imdb.com/name/nm0044741/"

    robot
      .buildViewModel()
      .mockFindById(Result.success(MediaItem.Unknown))
      .onHandleDeeplink(url)
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test handleDeepLink with imdb and findById failure does not emit anything`() = runTest {
    val url = "https://www.imdb.com/name/nm0044741/"

    robot
      .buildViewModel()
      .onHandleDeeplink(url)
      .assertUiEvent(MainUiEvent.None)
  }

  @Test
  fun `test refresh jellyseerr session is executed only once`() = runTest {
    robot
      .mockGetJellyseerrProfile(
        flowOf(
          Result.success(JellyseerrProfileResult("", null)),
          Result.success(
            JellyseerrProfileResult(
              "http://192.168.1.10:5055",
              JellyseerrProfileFactory.jellyseerr(),
            ),
          ),
        ),
      )
      .buildViewModel()
      .verifyJellyseerrRefreshSession(1)
  }
}
