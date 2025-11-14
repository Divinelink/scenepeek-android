package com.divinelink.scenepeek.popular.ui

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.scenepeek.home.ui.HomeFilter
import com.divinelink.scenepeek.home.ui.HomeViewState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

@Suppress("LargeClass")
class HomeViewModelTest {

  private val testRobot = HomeViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val popularMoviesList = MediaItemFactory.MoviesList()

  private val searchMovies = MediaItemFactory.MoviesList(10..20)

  @Test
  fun `successful initialise viewModel`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(emptyList()))
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `given popular movies list when I init then I expect Success Result`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(popularMoviesList),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          error = null,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `given offline error, when popular are already fetched I don't expect offline`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(popularMoviesList),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          error = null,
          retryAction = null,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
      .mockFetchPopularMovies(
        response = Result.failure(AppException.Offline()),
      )
      .onLoadNextPage()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          error = null,
          retryAction = null,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `given offline error, when popular movies are fetched, I expect offline state`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(AppException.Offline()),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          error = BlankSlateState.Offline,
          retryAction = HomeMode.Browser,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 1,
          ),
        ),
      )
  }

  @Test
  fun `test retryAction on browser when expecting failure does not change state`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(AppException.Offline()),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          error = BlankSlateState.Offline,
          retryAction = HomeMode.Browser,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 1,
          ),
        ),
      )
      .onRetryClick()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          error = BlankSlateState.Offline,
          retryAction = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 1,
          ),
        ),
      )
  }

  @Test
  fun `test retryAction on browser when expecting success, update popular data`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(AppException.Offline()),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          error = BlankSlateState.Offline,
          retryAction = HomeMode.Browser,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 1,
          ),
        ),
      )
      .mockFetchPopularMovies(response = Result.success(popularMoviesList))
      .onRetryClick()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          error = null,
          retryAction = null,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `given more data, when I loadNextPage then I expect accumulated data`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(popularMoviesList),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          error = null,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
      .mockFetchPopularMovies(
        response = Result.success(loadData(12, 20)),
      )
      .onLoadNextPage()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(
            data = popularMoviesList + loadData(12, 20),
            shouldLoadMore = true,
          ),
          error = null,
          pages = mapOf(
            HomePage.Popular to 3,
          ),
        ),
      )
  }

  @Test
  fun `duplicates are successfully removed`() = runTest {
    /**
     * If the time we fetch movies with IDs 1,2,3,4,5
     * and on our second attempt the IDs are 1,2,3,4,5,6,7,8,10
     * We'll merge those items to together but discard duplicate ones.
     * Favorite movies are in favor of non favorites.
     */
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(loadData(1, 5)),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = loadData(1, 5), shouldLoadMore = true),
          error = null,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
      .mockFetchPopularMovies(
        response = Result.success(loadData(1, 10)),
      )
      .onLoadNextPage()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = loadData(1, 10), shouldLoadMore = true),
          error = null,
          pages = mapOf(
            HomePage.Popular to 3,
          ),
        ),
      )
  }

  @Test
  fun `given error result when i init then i expect Error`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(Exception("oops")),
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState.initial().copy(
          isLoading = false,
          popularMovies = MediaSection(data = listOf(), shouldLoadMore = true),
          error = BlankSlateState.Generic,
        ),
      )
  }

  @Test
  fun `given a selected movie, when I mark it as favorite then update its status`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = popularMoviesList[0],
        result = Result.success(true),
      )
      .onMarkAsFavorite(
        movie = popularMoviesList[0],
      )
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `given unselected filter when I click filter then I expect filter to be selected`() =
    runTest {
      testRobot
        .mockFetchPopularMovies(
          response = Result.success(
            searchMovies,
          ),
        )
        .mockFetchFavoriteMovies(
          response = Result.success(
            searchMovies.filter { it.isFavorite == true },
          ),
        )
        .buildViewModel()
        .onFilterClicked(
          filter = HomeFilter.Liked.filter,
        )
        .assertViewState(
          expectedViewState = HomeViewState.initial().copy(
            popularMovies = MediaSection(data = searchMovies, shouldLoadMore = true),
            filteredResults = MediaSection(
              data = searchMovies.filter { it.isFavorite == true },
              shouldLoadMore = false,
            ),
            isLoading = false,
            filters = listOf(HomeFilter.Liked.filter.copy(isSelected = true)),
            mode = HomeMode.Filtered,
            pages = mapOf(
              HomePage.Popular to 2,
            ),
          ),
        )
        .onFilterClicked(filter = HomeFilter.Liked.filter)
        .assertViewState(
          expectedViewState = HomeViewState.initial().copy(
            popularMovies = MediaSection(data = searchMovies, shouldLoadMore = true),
            filteredResults = MediaSection(data = emptyList(), shouldLoadMore = false),
            isLoading = false,
            filters = listOf(HomeFilter.Liked.filter.copy(isSelected = false)),
            mode = HomeMode.Browser,
            pages = mapOf(
              HomePage.Popular to 2,
            ),
          ),
        )
    }

  @Test
  fun `when onClearFiltersClicked I expect all filters to be unselected`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(
          searchMovies,
        ),
      )
      .mockFetchFavoriteMovies(
        response = Result.success(
          searchMovies.filter { it.isFavorite == true },
        ),
      )
      .buildViewModel()
      .onFilterClicked(
        filter = HomeFilter.Liked.filter,
      )
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = searchMovies, shouldLoadMore = true),
          filteredResults = MediaSection(
            data = searchMovies.filter { it.isFavorite == true },
            shouldLoadMore = false,
          ),
          isLoading = false,
          filters = listOf(HomeFilter.Liked.filter.copy(isSelected = true)),
          mode = HomeMode.Filtered,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
      .onClearFiltersClicked()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = searchMovies, shouldLoadMore = true),
          filteredResults = null,
          isLoading = false,
          filters = HomeFilter.entries.map { it.filter },
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  @Test
  fun `when clicking on filter that is now known doesn't do anything`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(
          searchMovies,
        ),
      )
      .buildViewModel()
      .onFilterClicked(
        filter = HomeFilter.Liked.filter.copy(name = "Unknown"),
      )
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = searchMovies, shouldLoadMore = true),
          isLoading = false,
          filters = listOf(HomeFilter.Liked.filter),
          pages = mapOf(
            HomePage.Popular to 2,
          ),
        ),
      )
  }

  private fun loadData(
    starting: Int,
    ending: Int,
  ): List<MediaItem.Media.Movie> = (starting..ending).map {
    MediaItemFactory.FightClub().toWizard {
      withId(it)
    }
  }.toList()
}
