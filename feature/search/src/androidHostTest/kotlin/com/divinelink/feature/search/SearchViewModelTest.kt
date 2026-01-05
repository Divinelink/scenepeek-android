package com.divinelink.feature.search

import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.feature.search.ui.SearchForm
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
            tab = SearchTab.All,
            page = 1,
            canFetchMore = false,
          ),
        ),
      )
      .buildViewModel()
      .onSearch("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> null
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          query = "test query",
          isLoading = false,
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
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
            tab = SearchTab.All,
            page = 1,
            canFetchMore = false,
          ),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("tes")
      .onSearch("test ")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test ",
          isLoading = true,
        ),
      )
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          query = "test query",
          isLoading = false,
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
  }

  @Test
  fun `test clearing query successfully sets state to initial`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = searchMovies,
            tab = SearchTab.All,
            page = 1,
            canFetchMore = false,
          ),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> null
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
      .delay(400)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to searchMovies),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          query = "test query",
          isLoading = false,
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
        ),
      )
      .onClearClicked()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "",
          isLoading = false,
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Initial
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> null
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 0
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
        ),
      )
  }

  @Test
  fun `given loading state when I search then I expect SearchLoading to be true`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = emptyList(),
            tab = SearchTab.All,
            page = 1,
            canFetchMore = false,
          ),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> null
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to emptyList()),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
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
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Error.Network
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
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
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Error.Network
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
            tab = SearchTab.All,
            page = 1,
            canFetchMore = false,
          ),
        ),
      )
      .onRetryClick()
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `given offline error, when search are already fetched I don't expect offline`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(createResponse()),
      )
      .buildViewModel()
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
      .mockFetchSearchMedia(response = Result.failure(UnknownHostException("You are offline")))
      .onLoadNextPage()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `given empty search results when I search then I emptyResult`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          createResponse(list = emptyList()),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to emptyList()),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `given user is already searching when LoadingNextPage then I Expect fetchFromSearchQuery`() =
    runTest {
      testRobot
        .mockFetchSearchMedia(
          response = Result.success(
            createResponse(list = searchMovies),
          ),
        )
        .buildViewModel()
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            isLoading = false,
          ),
        )
        .onSearch("test query")
        .delay(300)
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            forms = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> SearchForm.Data(
                  pages = mapOf(1 to searchMovies),
                )
                SearchTab.Movie -> SearchForm.Initial
                SearchTab.People -> SearchForm.Initial
                SearchTab.TV -> SearchForm.Initial
              }
            },
            canFetchMore = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> false
                SearchTab.Movie -> true
                SearchTab.People -> true
                SearchTab.TV -> true
              }
            },
            pages = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> 1
                SearchTab.Movie -> 0
                SearchTab.People -> 0
                SearchTab.TV -> 0
              }
            },
            lastQuery = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> "test query"
                SearchTab.Movie -> null
                SearchTab.People -> null
                SearchTab.TV -> null
              }
            },
            query = "test query",
            isLoading = false,
          ),
        )
        .onLoadNextPage()
        .delay(300)
        .assertUiState(
          expectedViewState = SearchUiState.initial().copy(
            forms = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> SearchForm.Data(
                  pages = mapOf(1 to searchMovies),
                )
                SearchTab.Movie -> SearchForm.Initial
                SearchTab.People -> SearchForm.Initial
                SearchTab.TV -> SearchForm.Initial
              }
            },
            canFetchMore = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> false
                SearchTab.Movie -> true
                SearchTab.People -> true
                SearchTab.TV -> true
              }
            },
            pages = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> 1
                SearchTab.Movie -> 0
                SearchTab.People -> 0
                SearchTab.TV -> 0
              }
            },
            lastQuery = SearchTab.entries.associateWith { tab ->
              when (tab) {
                SearchTab.All -> "test query"
                SearchTab.Movie -> null
                SearchTab.People -> null
                SearchTab.TV -> null
              }
            },
            query = "test query",
            isLoading = false,
          ),
        )
    }

  @Test
  fun `test pagination on search mode`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          createResponse(list = searchMovies),
        ),
      )
      .buildViewModel()
      .onSearch("test query")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = true,
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to searchMovies),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
      .mockFetchSearchMedia(
        response = Result.success(
          createResponse(
            page = 2,
            list = loadData(21, 30),
          ),
        ),
      )
      .onLoadNextPage()
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(
                  1 to searchMovies,
                  2 to loadData(21, 30),
                ),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 2
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `clearing query successfully cancels search job`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          createResponse(list = searchMovies),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("test ")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test ",
          isLoading = true,
        ),
      )
      .onSearch("test query")
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
  fun `given generic error state when I search then I expect error result`() = runTest {
    testRobot
      .mockFetchSearchMedia(Result.failure(Exception("Oops.")))
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          isLoading = false,
        ),
      )
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "test query",
          isLoading = false,
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Error.Unknown
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
        ),
      )
  }

  @Test
  fun `given empty query when onSearchMovies then I expect onClearClicked`() = runTest {
    testRobot
      .mockFetchSearchMedia(
        response = Result.success(
          createResponse(
            list = searchMovies,
          ),
        ),
      )
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(),
      )
      .onSearch("")
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          query = "",
        ),
      )
  }

  @Test
  fun `test select tab updates tab index`() = runTest {
    testRobot
      .buildViewModel()
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(selectedTabIndex = 0),
      )
      .onSelectTab(tab = SearchTab.Movie)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(selectedTabIndex = 1),
      )
  }

  @Test
  fun `test search and select tab searches for the selected tab with current query`() = runTest {
    testRobot
      .buildViewModel()
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            list = popularMoviesList,
          ),
        ),
      )
      .onSearch("test query")
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            list = searchMovies,
            tab = SearchTab.Movie,
          ),
        ),
      )
      .onSelectTab(tab = SearchTab.Movie)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Loading
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          selectedTabIndex = 1,
          isLoading = true,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
        ),
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Data(
                pages = mapOf(1 to searchMovies),
              )
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Initial
            }
          },
          selectedTabIndex = 1,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> false
              SearchTab.People -> true
              SearchTab.TV -> true
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 1
              SearchTab.People -> 0
              SearchTab.TV -> 0
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> "test query"
              SearchTab.People -> null
              SearchTab.TV -> null
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
  }

  @Test
  fun `test update query on already fetched pages clears data for other pages`() = runTest {
    testRobot
      .buildViewModel()
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            list = popularMoviesList,
          ),
        ),
      )
      .onSearch("test query")
      .delay(300)
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            list = searchMovies,
            tab = SearchTab.TV,
          ),
        ),
      )
      .onSelectTab(tab = SearchTab.TV)
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Data(
                pages = mapOf(1 to searchMovies),
              )
            }
          },
          selectedTabIndex = 2,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> false
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 1
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> "test query"
            }
          },
          query = "test query",
          isLoading = false,
        ),
      )
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            list = loadData(
              starting = 15,
              ending = 30,
            ),
            tab = SearchTab.TV,
          ),
        ),
      )
      .onSearch("new q", reset = true)
      .onSearch("new que", reset = true)
      .onSearch("new query", reset = true)
      .delay(350)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to popularMoviesList),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Data(
                pages = mapOf(1 to loadData(starting = 15, ending = 30)),
              )
            }
          },
          selectedTabIndex = 2,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> false
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 0
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 1
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> "new query"
            }
          },
          query = "new query",
          isLoading = false,
        ),
      )
      .mockFetchSearchMedia(
        Result.success(
          createResponse(
            query = "new query",
            list = loadData(1, 5),
          ),
        ),
      )
      .onSelectTab(SearchTab.All)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Loading
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Data(
                pages = mapOf(1 to loadData(starting = 15, ending = 30)),
              )
            }
          },
          selectedTabIndex = 0,
          isLoading = true,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> false
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 0
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 1
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "test query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> "new query"
            }
          },
          query = "new query",
        )
      )
      .delay(300)
      .assertUiState(
        expectedViewState = SearchUiState.initial().copy(
          forms = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> SearchForm.Data(
                pages = mapOf(1 to loadData(starting = 1, ending = 5)),
              )
              SearchTab.Movie -> SearchForm.Initial
              SearchTab.People -> SearchForm.Initial
              SearchTab.TV -> SearchForm.Data(
                pages = mapOf(1 to loadData(starting = 15, ending = 30)),
              )
            }
          },
          selectedTabIndex = 0,
          canFetchMore = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> false
              SearchTab.Movie -> true
              SearchTab.People -> true
              SearchTab.TV -> false
            }
          },
          pages = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> 1
              SearchTab.Movie -> 0
              SearchTab.People -> 0
              SearchTab.TV -> 1
            }
          },
          lastQuery = SearchTab.entries.associateWith { tab ->
            when (tab) {
              SearchTab.All -> "new query"
              SearchTab.Movie -> null
              SearchTab.People -> null
              SearchTab.TV -> "new query"
            }
          },
          query = "new query",
          isLoading = false,
        ),
      )
  }

  private fun createResponse(
    query: String = "test query",
    list: List<MediaItem.Media> = popularMoviesList,
    tab: SearchTab = SearchTab.All,
    page: Int = 1,
    canFetchMore: Boolean = false,
  ) = MultiSearchResult(
    query = query,
    searchList = list,
    tab = tab,
    page = page,
    canFetchMore = canFetchMore,
  )

  private fun loadData(
    starting: Int,
    ending: Int,
  ): List<MediaItem.Media.Movie> = (starting..ending).map {
    MediaItemFactory.FightClub().toWizard {
      withId(it)
    }
  }.toList()
}
