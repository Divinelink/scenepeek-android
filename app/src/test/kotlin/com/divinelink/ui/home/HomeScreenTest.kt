package com.divinelink.ui.home

import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.performClick
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
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
        onNavigateToSettings = {},
        onNavigateToDetails = {
          detailsRoute = it
        },
        onNavigateToPerson = {},
        animatedVisibilityScope = this,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
          searchStateManager = SearchStateManager(),
        ),
        onNavigateToSearch = {},
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
}
