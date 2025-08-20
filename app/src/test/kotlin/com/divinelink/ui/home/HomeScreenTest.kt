package com.divinelink.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.TestTags.MEDIA_LIST_TAG
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.scenepeek.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.scenepeek.home.ui.HomeScreen
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class HomeScreenTest : ComposeTest() {

  private val getPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private val getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun navigateToDetailsScreenTest() = runTest {
    var detailsRoute: DetailsRoute? = null
    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(MediaItemFactory.MoviesList())),
    )

    setVisibilityScopeContent {
      HomeScreen(
        onNavigate = {
          if (it is DetailsRoute) {
            detailsRoute = it
          }
        },
        animatedVisibilityScope = this,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
          searchStateManager = SearchStateManager(),
        ),
      )
    }
    composeTestRule
      .onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
      .performClick()

    assertThat(detailsRoute).isEqualTo(
      DetailsRoute(
        id = 1,
        isFavorite = false,
        mediaType = MediaType.MOVIE,
      ),
    )
  }

  @Test
  fun `test on media long click opens action modal`() = runTest {
    var route: Navigation? = null
    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(MediaItemFactory.MoviesList())),
    )

    val viewModel = HomeViewModel(
      getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
      markAsFavoriteUseCase = markAsFavoriteUseCase,
      getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      searchStateManager = SearchStateManager(),
    )

    setVisibilityScopeContent {
      HomeScreen(
        onNavigate = { route = it },
        animatedVisibilityScope = this,
        viewModel = viewModel,
      )
    }
    composeTestRule
      .onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
      .performTouchInput {
        longClick()
      }

    assertThat(route).isEqualTo(
      Navigation.ActionMenuRoute.Media(MediaItemFactory.MoviesList().first().encodeToString()),
    )
  }

  @Test
  fun `test switch between browser and filter mode`() = runTest {
    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(MediaItemFactory.MoviesList(1..20))),
    )

    getFavoriteMoviesUseCase.mockGetFavoriteMovies(
      response = Result.success(MediaItemFactory.all()),
    )

    val viewModel = HomeViewModel(
      getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
      markAsFavoriteUseCase = markAsFavoriteUseCase,
      getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
      searchStateManager = SearchStateManager(),
    )

    setVisibilityScopeContent {
      HomeScreen(
        onNavigate = {},
        animatedVisibilityScope = this,
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Fight club 1").assertIsDisplayed()
      onNodeWithTag(MEDIA_LIST_TAG).performScrollToIndex(19)
      onNodeWithText("Fight club 20").assertIsDisplayed()

      onNodeWithText("Liked By You").performClick()
      onNodeWithText("Fight club 20").assertIsNotDisplayed()
      onNodeWithText("The Wire").assertIsDisplayed()
      onNodeWithTag(TestTags.Components.Button.CLEAR_FILTERS).performClick()
      onNodeWithText("Fight club 20").assertIsDisplayed()
      onNodeWithText("The Wire").assertIsNotDisplayed()
    }
  }
}
