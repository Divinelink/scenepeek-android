package com.andreolas.movierama.popular.ui

import com.andreolas.movierama.home.domain.usecase.MultiSearchResult
import com.andreolas.movierama.home.ui.HomeFilter
import com.andreolas.movierama.home.ui.HomeViewState
import com.andreolas.movierama.home.ui.MediaSection
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.divinelink.core.testing.factories.model.media.MediaItemFactory.toWizard
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.net.UnknownHostException
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
          ),
        ),
      )
      .mockFetchPopularMovies(
        response = Result.failure(UnknownHostException("You are offline")),
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
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `given offline error, when popular movies are fetched, I expect offline state`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(UnknownHostException("You are offline")),
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
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `test retryAction on browser when expecting failure does not change state`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(UnknownHostException("You are offline")),
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `test retryAction on browser when expecting success, update popular data`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(UnknownHostException("You are offline")),
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
          error = null,
        ),
      )
  }

  @Test
  fun `given search data, when I search movies then I expect success result`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(emptyList()))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
            totalPages = 1,
          ),
        ),
      )
      .buildViewModel()
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = true,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = false),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `search job is correctly delayed when user types fast`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(emptyList()))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
            totalPages = 1,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("tes")
      .onSearchMovies("test ")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          query = "test ",
          isSearchLoading = true,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = false),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `clearing query successfully cancels search job`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(emptyList()))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = searchMovies,
            totalPages = 1,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test ")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          query = "test ",
          isSearchLoading = true,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(200)
      .onClearClicked()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = emptyList(), shouldLoadMore = true),
          isLoading = false,
          query = "",
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `clearing query successfully loads cached movies`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = searchMovies,
            totalPages = 1,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = true,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .delay(400)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
      .onClearClicked()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "",
          isSearchLoading = false,
          searchResults = null,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `given loading state when I search then I expect SearchLoading to be true`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        Result.success(
          MultiSearchResult(
            "test query",
            emptyList(),
            totalPages = 2,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = true,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          searchResults = MediaSection(data = emptyList(), shouldLoadMore = true),
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `given generic error state when I search then I expect no error result`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(Result.failure(Exception("Oops.")))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = null,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `given offline error state when I search then I expect error result`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(Result.failure(UnknownHostException("You are offline.")))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = BlankSlateState.Offline,
          mode = HomeMode.Search,
          retryAction = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `test retry on search when expecting success`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(Result.failure(UnknownHostException("You are offline.")))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = BlankSlateState.Offline,
          mode = HomeMode.Search,
          retryAction = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
            totalPages = 2,
          ),
        ),
      )
      .onRetryClick()
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = null,
          retryAction = null,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `given offline error, when search are already fetched I don't expect offline`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
            totalPages = 2,
          ),
        ),
      )
      .buildViewModel()
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = null,
          retryAction = null,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
      .mockFetchSearchMedia(response = Result.failure(UnknownHostException("You are offline")))
      .onLoadNextPage()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          error = null,
          retryAction = null,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `given empty search results when I search then I emptyResult`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = emptyList(),
            totalPages = 1,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = emptyList(), shouldLoadMore = false),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
  }

  @Test
  fun `given user is already searching when LoadingNextPage then I Expect fetchFromSearchQuery`() =
    runTest {
      testRobot
        .mockFetchPopularMovies(Result.success(popularMoviesList))
        .mockFetchSearchMedia(
          response = Result.success(
            MultiSearchResult(
              query = "test query",
              searchList = searchMovies,
              totalPages = 1,
            ),
          ),
        )
        .buildViewModel()
        .assertViewState(
          expectedViewState = HomeViewState.initial().copy(
            popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
            isLoading = false,
            isSearchLoading = false,
            pages = mapOf(
              HomePage.Popular to 2,
              HomePage.Search to 1,
            ),
          ),
        )
        .onSearchMovies("test query")
        .delay(300)
        .assertViewState(
          expectedViewState = HomeViewState.initial().copy(
            popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
            searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
            isLoading = false,
            query = "test query",
            isSearchLoading = false,
            mode = HomeMode.Search,
            pages = mapOf(
              HomePage.Popular to 2,
              HomePage.Search to 2,
            ),
          ),
        )
        .mockFetchPopularMovies(Result.success(searchMovies.plus(searchMovies)))
        .onLoadNextPage()
        .delay(300)
        .assertViewState(
          expectedViewState = HomeViewState.initial().copy(
            popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
            searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
            isLoading = false,
            query = "test query",
            isSearchLoading = false,
            mode = HomeMode.Search,
            pages = mapOf(
              HomePage.Popular to 2,
              HomePage.Search to 2,
            ),
          ),
        )
    }

  @Test
  fun `given empty query when onSearchMovies then I expect onClearClicked`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = searchMovies,
            totalPages = 0,
          ),
        ),
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .onSearchMovies("")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "",
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
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
          isSearchLoading = false,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .mockMarkAsFavorite(
        mediaItem = popularMoviesList[0],
        result = Result.success(Unit),
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
            HomePage.Search to 1,
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
              HomePage.Search to 1,
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
              HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
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
            HomePage.Search to 1,
          ),
        ),
      )
  }

  @Test
  fun `test pagination on search mode`() = runTest {
    testRobot
      .mockFetchPopularMovies(response = Result.success(popularMoviesList))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = searchMovies,
            totalPages = 2,
          ),
        ),
      )
      .buildViewModel()
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = true,
          mode = HomeMode.Browser,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 1,
          ),
        ),
      )
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(data = searchMovies, shouldLoadMore = true),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 2,
          ),
        ),
      )
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = loadData(21, 30),
            totalPages = 2,
          ),
        ),
      )
      .onLoadNextPage()
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState.initial().copy(
          popularMovies = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          searchResults = MediaSection(
            data = searchMovies + loadData(21, 30),
            shouldLoadMore = false,
          ),
          isLoading = false,
          query = "test query",
          isSearchLoading = false,
          mode = HomeMode.Search,
          pages = mapOf(
            HomePage.Popular to 2,
            HomePage.Search to 3,
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
