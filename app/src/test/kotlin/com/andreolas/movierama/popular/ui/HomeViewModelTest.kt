package com.andreolas.movierama.popular.ui

import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.home.domain.usecase.MultiSearchResult
import com.andreolas.movierama.home.ui.HomeFilter
import com.andreolas.movierama.home.ui.HomeViewState
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
        HomeViewState(
          isLoading = false,
          popularMovies = listOf(),
          error = null,
        )
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
        HomeViewState(
          isLoading = false,
          popularMovies = popularMoviesList,
          error = null,
        )
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
        HomeViewState(
          isLoading = false,
          popularMovies = popularMoviesList,
          error = null,
        )
      )
      .mockFetchPopularMovies(
        response = Result.success(loadData(12, 20))
      )
      .onLoadNextPage()
      .assertViewState(
        HomeViewState(
          isLoading = false,
          popularMovies = popularMoviesList + loadData(12, 20),
          error = null,
        )
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
        response = Result.success(loadData(1, 5))
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState(
          isLoading = false,
          popularMovies = loadData(1, 5),
          error = null,
        )
      )
      .mockFetchPopularMovies(
        response = Result.success(loadData(1, 10))
      )
      .onLoadNextPage()
      .assertViewState(
        HomeViewState(
          isLoading = false,
          popularMovies = loadData(1, 10),
          error = null,
        )
      )
  }

  @Test
  fun `given error result when i init then i expect Error`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.failure(Exception("oops"))
      )
      .buildViewModel()
      .assertViewState(
        HomeViewState(
          isLoading = false,
          popularMovies = listOf(),
          error = UIText.StringText("oops")
        )
      )
  }

//    @Test
//    fun `given a list of movies, when I mark a movie as favorite then i expect updated favorite status`() = runTest {
//        testRobot
//            .mockFetchPopularMovies(Result.success(popularMoviesList))
//            .buildViewModel()
//            .mockMarkAsFavorite(
//                result = Result.success(Unit)
//            )
//            .onMarkAsFavorite(popularMoviesList[5])
//            .assertViewState(
//                HomeViewState(
//                    isLoading = false,
//                    popularMovies = popularMoviesList.apply {
//                        this[5] = this[5].copy(isFavorite = true)
//                    },
//
//                    error = null
//
//                )
//            )
//            .onMarkAsFavorite(popularMoviesList[2])
//            .assertViewState(
//                HomeViewState(
//                    isLoading = false,
//                    popularMovies = popularMoviesList.apply {
//                        this[2] = this[2].copy(isFavorite = true)
//                    },
//
//                    error = null
//
//                )
//            )
//            .assertFalseViewState(
//                HomeViewState(
//                    isLoading = false,
//                    popularMovies = popularMoviesList.apply {
//                        this[2] = this[2].copy(isFavorite = false)
//                    },
//
//                    error = null
//
//                )
//            )
//    }

//    @Test
//    fun `given a list of movies, when I unMark a movie as favorite then i expect updated favorite status`() = runTest {
//        testRobot
//            .mockFetchPopularMovies(Result.success(popularMoviesList))
//            .buildViewModel()
//            .mockMarkAsFavorite(
//                result = Result.success(Unit)
//            )
//            .mockRemoveFavorite(
//                result = Result.success(Unit)
//            )
//            .onMarkAsFavorite(popularMoviesList[5])
//            .assertViewState(
//                HomeViewState(
//                    isLoading = false,
//                    popularMovies = popularMoviesList.apply {
//                        this[5] = this[5].copy(isFavorite = true)
//                    },
//
//                    error = null
//
//                )
//            )
//            .onMarkAsFavorite(popularMoviesList[5])
//            .assertViewState(
//                HomeViewState(
//                    isLoading = false,
//                    popularMovies = popularMoviesList.apply {
//                        this[5] = this[5].copy(isFavorite = false)
//                    },
//
//                    error = null
//                )
//            )
//    }

  @Test
  fun `Given Search Data, when I searchMovies then I expect Success Result`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(emptyList()))
      .mockFetchSearchMedia(
        response = Result.success(
          MultiSearchResult(
            query = "test query",
            searchList = popularMoviesList,
          )
        )
      )
      .buildViewModel()
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = true,
          emptyResult = false,
        )
      )
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          searchResults = popularMoviesList,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = false,
        )
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
          )
        )
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("tes")
      .onSearchMovies("test ")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          isLoading = false,
          loadMorePopular = false,
          query = "test ",
          searchLoadingIndicator = true,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          searchResults = popularMoviesList,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = false,
        )
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
          )
        )
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("test ")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          isLoading = false,
          loadMorePopular = false,
          query = "test ",
          searchLoadingIndicator = true,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .delay(200)
      .onClearClicked()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = emptyList(),
          loadMorePopular = true,
          isLoading = false,
          query = "",
          searchLoadingIndicator = false,
          emptyResult = false,
        )
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
          )
        )
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = true,
          emptyResult = false,
        )
      )
      .delay(400)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          searchResults = searchMovies,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onClearClicked()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = true,
          isLoading = false,
          query = "",
          searchLoadingIndicator = false,
          emptyResult = false,
          searchResults = null,
        )
      )
  }

  @Test
  fun `given loading state when I search then I expect SearchLoading to be true`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(Result.success(MultiSearchResult("test query", emptyList())))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = true,
        )
      )
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = true,
          searchResults = emptyList(),
        )
      )
  }

  @Test
  fun `given error state when I search then I expect Error Result`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .mockFetchSearchMedia(Result.failure(Exception("Oops.")))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = false,
          isLoading = true,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = false,
          error = UIText.StringText("Oops.")
        )
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
          )
        )
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("test query")
      .delay(300)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          searchResults = emptyList(),
          loadMorePopular = false,
          isLoading = false,
          query = "test query",
          searchLoadingIndicator = false,
          emptyResult = true,
        )
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
            )
          )
        )
        .buildViewModel()
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            isLoading = false,
            loadMorePopular = true,
            searchLoadingIndicator = false,
            emptyResult = false,
          )
        )
        .onSearchMovies("test query")
        .delay(300)
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            searchResults = searchMovies,
            loadMorePopular = false,
            isLoading = false,
            query = "test query",
            searchLoadingIndicator = false,
            emptyResult = false,
          )
        )
        .mockFetchPopularMovies(Result.success(searchMovies.plus(searchMovies)))
        .onLoadNextPage()
        .delay(300)
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            searchResults = searchMovies,
            loadMorePopular = false,
            isLoading = false,
            query = "test query",
            searchLoadingIndicator = false,
            emptyResult = false,
          )
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
          )
        )
      )
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .onSearchMovies("")
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          loadMorePopular = true,
          isLoading = false,
          query = "",
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
  }

  @Test
  fun `selecting a movie successfully updates selectedMovie`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
        )
      )
  }

  @Test
  fun `selecting the same movie sets selectedMovie movie to null`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
        )
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
        )
      )
  }

  @Test
  fun `selecting movies within debounce time discards them`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .delay(50)
      .delay(50)
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
        )
      )
  }

  @Test
  fun `given a selected movie, when I swipe down then Ie null expect selectedMovie to be null`() =
    runTest {
      testRobot
        .mockFetchPopularMovies(Result.success(popularMoviesList))
        .buildViewModel()
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            isLoading = false,
            loadMorePopular = true,
            searchLoadingIndicator = false,
            emptyResult = false,
          )
        )
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            isLoading = false,
          )
        )
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = popularMoviesList,
            isLoading = false,
          )
        )
    }

  @Test
  fun `given a selected movie, when I mark it as favorite then update its status`() = runTest {
    testRobot
      .mockFetchPopularMovies(Result.success(popularMoviesList))
      .buildViewModel()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
          loadMorePopular = true,
          searchLoadingIndicator = false,
          emptyResult = false,
        )
      )
      .mockMarkAsFavorite(
        mediaItem = popularMoviesList[0],
        result = Result.success(Unit),
      )
      .onMarkAsFavorite(
        movie = popularMoviesList[0],
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = popularMoviesList,
          isLoading = false,
        )
      )
  }

  @Test
  fun `given unselected filter when I click filter then I expect filter to be selected`() =
    runTest {
      testRobot
        .mockFetchPopularMovies(
          response = Result.success(
            searchMovies
          )
        )
        .mockFetchFavoriteMovies(
          response = Result.success(
            searchMovies.filter { it.isFavorite == true }
          )
        )
        .buildViewModel()
        .onFilterClicked(
          filter = HomeFilter.Liked.filter.name
        )
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = searchMovies,
            filteredResults = searchMovies.filter { it.isFavorite == true },
            isLoading = false,
            filters = listOf(HomeFilter.Liked.filter.copy(isSelected = true)),
          )
        )
        .onFilterClicked(
          filter = HomeFilter.Liked.filter.name
        )
        .assertViewState(
          expectedViewState = HomeViewState(
            popularMovies = searchMovies,
            filteredResults = emptyList(),
            isLoading = false,
            filters = listOf(HomeFilter.Liked.filter.copy(isSelected = false)),
          )
        )
    }

  @Test
  fun `when onClearFiltersClicked I expect all filters to be unselected`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(
          searchMovies
        )
      )
      .mockFetchFavoriteMovies(
        response = Result.success(
          searchMovies.filter { it.isFavorite == true }
        )
      )
      .buildViewModel()
      .onFilterClicked(
        filter = HomeFilter.Liked.filter.name
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = searchMovies,
          filteredResults = searchMovies.filter { it.isFavorite == true },
          isLoading = false,
          filters = listOf(HomeFilter.Liked.filter.copy(isSelected = true)),
        )
      )
      .onClearFiltersClicked()
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = searchMovies,
          filteredResults = null,
          isLoading = false,
          filters = HomeFilter.entries.map { it.filter },
        )
      )
  }

  @Test
  fun `when clicking on filter that is now known doesn't do anything`() = runTest {
    testRobot
      .mockFetchPopularMovies(
        response = Result.success(
          searchMovies
        )
      )
      .buildViewModel()
      .onFilterClicked(
        filter = "Some filter"
      )
      .assertViewState(
        expectedViewState = HomeViewState(
          popularMovies = searchMovies,
          isLoading = false,
          filters = listOf(HomeFilter.Liked.filter),
        )
      )
  }

  private fun loadData(starting: Int, ending: Int): List<MediaItem.Media.Movie> {
    return (starting..ending).map {
      MediaItemFactory.FightClub().toWizard {
        withId(it)
      }
    }.toList()
  }
}