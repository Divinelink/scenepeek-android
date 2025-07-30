package com.divinelink.feature.search

import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.search.ui.SearchUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.net.UnknownHostException
import kotlin.test.Test

class SearchViewModelTest {

  private val testRobot = SearchViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val popularMoviesList = MediaItemFactory.MoviesList()

  private val searchMovies = MediaItemFactory.MoviesList(10..20)

  @Test
  fun `given search data, when I search movies then I expect success result`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = false),
          query = "test query",
          isLoading = false,
          page = 2,
        ),
      )
  }

  @Test
  fun `search job is correctly delayed when user types fast`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("tes")
      .onSearchMovies("test ")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test ",
          isLoading = true,
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = false),
          query = "test query",
          isLoading = false,
          page = 2,
        ),
      )
  }

  @Test
  fun `clearing query successfully loads cached movies`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
        ),
      )
      .delay(400)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
          query = "test query",
          isLoading = false,
          page = 2,
        ),
      )
      .onClearClicked()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "",
          isLoading = false,
          searchResults = null,
          page = 1,
        ),
      )
  }

  @Test
  fun `given loading state when I search then I expect SearchLoading to be true`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          searchResults = MediaSection(data = emptyList(), shouldLoadMore = true),
          page = 2,
        ),
      )
  }

  @Test
  fun `given offline error state when I search then I expect error result`() = runTest {
    testRobot
      .mockFetchSearchMedia(Result.failure(AppException.Offline()))
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          error = BlankSlateState.Offline,
        ),
      )
  }

  @Test
  fun `test retry on search when expecting success`() = runTest {
    testRobot
      .mockFetchSearchMedia(Result.failure(AppException.Offline()))
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          error = BlankSlateState.Offline,
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          query = "test query",
          isLoading = false,
          error = null,
          page = 2,
        ),
      )
  }

  @Test
  fun `given offline error, when search are already fetched I don't expect offline`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          query = "test query",
          isLoading = false,
          error = null,
          page = 2,
        ),
      )
      .mockFetchSearchMedia(response = Result.failure(UnknownHostException("You are offline")))
      .onLoadNextPage()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = popularMoviesList, shouldLoadMore = true),
          query = "test query",
          isLoading = false,
          error = null,
          page = 2,
        ),
      )
  }

  @Test
  fun `given empty search results when I search then I emptyResult`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = emptyList(), shouldLoadMore = false),
          query = "test query",
          isLoading = false,
          page = 2,
        ),
      )
  }

  @Test
  fun `given user is already searching when LoadingNextPage then I Expect fetchFromSearchQuery`() =
    runTest {
      testRobot
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
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            isLoading = false,
          ),
        )
        .onSearchMovies("test query")
        .delay(300)
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
            query = "test query",
            isLoading = false,
            page = 2,
          ),
        )
        .onLoadNextPage()
        .delay(300)
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            searchResults = MediaSection(data = searchMovies, shouldLoadMore = false),
            query = "test query",
            isLoading = false,
            page = 3,
          ),
        )
    }

  @Test
  fun `test pagination on search mode`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(data = searchMovies, shouldLoadMore = true),
          query = "test query",
          isLoading = false,
          page = 2,
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          searchResults = MediaSection(
            data = searchMovies + loadData(21, 30),
            shouldLoadMore = false,
          ),
          query = "test query",
          isLoading = false,
          page = 3,
        ),
      )
  }

  @Test
  fun `clearing query successfully cancels search job`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test ")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test ",
          isLoading = true,
        ),
      )
      .onSearchMovies("test query")
      .delay(200)
      .onClearClicked()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `given generic error state when I search then I expect no error result`() = runTest {
    testRobot
      .mockFetchSearchMedia(Result.failure(Exception("Oops.")))
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          error = null,
        ),
      )
  }

  @Test
  fun `given empty query when onSearchMovies then I expect onClearClicked`() = runTest {
    testRobot
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
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(),
      )
      .onSearchMovies("")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "",
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
