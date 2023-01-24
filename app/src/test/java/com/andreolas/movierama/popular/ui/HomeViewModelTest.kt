package com.andreolas.movierama.popular.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.ui.HomeViewState
import com.andreolas.movierama.ui.UIText
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testRobot = HomeViewModelTestRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val popularMoviesList = (1..10).map {
        PopularMovie(id = it, posterPath = "", releaseDate = "", title = "", rating = "", isFavorite = false)
    }.toMutableList()

    @Test
    fun `successful initialise viewModel`() = runTest {
        testRobot
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = true,
                    moviesList = listOf(),
                    selectedMovie = null,
                    error = null,
                )
            )
    }

    @Test
    fun `given popular movies list when I init then I expect Success Result`() = runTest {
        testRobot
            .mockFetchPopularMovies(
                response = Result.Success(popularMoviesList),
            )
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList,
                    selectedMovie = null,
                    error = null,
                )
            )
    }

    @Test
    fun `given more data, when I loadNextPage then I expect accumulated data`() = runTest {
        testRobot
            .mockFetchPopularMovies(
                response = Result.Success(popularMoviesList),
            )
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList,
                    selectedMovie = null,
                    error = null,
                )
            )
            .mockFetchPopularMovies(
                response = Result.Success(loadData(12, 20))
            )
            .onLoadNextPage()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList + loadData(12, 20),
                    selectedMovie = null,
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
                response = Result.Success(loadData(1, 5))
            )
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = loadData(1, 5),
                    selectedMovie = null,
                    error = null,
                )
            )
            .mockFetchPopularMovies(
                response = Result.Success(loadData(1, 10))
            )
            .onLoadNextPage()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = loadData(1, 10),
                    selectedMovie = null,
                    error = null,
                )
            )
    }

    @Test
    fun `given error result when i init then i expect Error`() = runTest {
        testRobot
            .mockFetchPopularMovies(
                response = Result.Error(Exception("oops"))
            )
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = listOf(),
                    selectedMovie = null,
                    error = UIText.StringText("oops")
                )
            )
    }

    @Test
    fun `given loading result when i init then i expect loading state`() = runTest {
        testRobot
            .mockFetchPopularMovies(
                response = Result.Loading
            )
            .buildViewModel()
            .assertViewState(
                HomeViewState(
                    isLoading = true,
                    moviesList = listOf(),
                )
            )
    }

    @Test
    fun `given a list of movies, when I mark a movie as favorite then i expect updated favorite status`() = runTest {
        testRobot
            .mockFetchPopularMovies(Result.Success(popularMoviesList))
            .buildViewModel()
            .mockMarkAsFavorite(
                result = Result.Success(Unit)
            )
            .onMarkAsFavorite(popularMoviesList[5])
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList.apply {
                        this[5] = this[5].copy(isFavorite = true)
                    },
                    selectedMovie = null,
                    error = null

                )
            )
            .onMarkAsFavorite(popularMoviesList[2])
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList.apply {
                        this[2] = this[2].copy(isFavorite = true)
                    },
                    selectedMovie = null,
                    error = null

                )
            )
            .assertFalseViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList.apply {
                        this[2] = this[2].copy(isFavorite = false)
                    },
                    selectedMovie = null,
                    error = null

                )
            )
    }

    @Test
    fun `given a list of movies, when I unMark a movie as favorite then i expect updated favorite status`() = runTest {
        testRobot
            .mockFetchPopularMovies(Result.Success(popularMoviesList))
            .buildViewModel()
            .mockMarkAsFavorite(
                result = Result.Success(Unit)
            )
            .mockRemoveFavorite(
                result = Result.Success(Unit)
            )
            .onMarkAsFavorite(popularMoviesList[5])
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList.apply {
                        this[5] = this[5].copy(isFavorite = true)
                    },
                    selectedMovie = null,
                    error = null

                )
            )
            .onMarkAsFavorite(popularMoviesList[5])
            .assertViewState(
                HomeViewState(
                    isLoading = false,
                    moviesList = popularMoviesList.apply {
                        this[5] = this[5].copy(isFavorite = false)
                    },
                    selectedMovie = null,
                    error = null
                )
            )
    }

    private fun loadData(starting: Int, ending: Int): List<PopularMovie> {
        return (starting..ending).map {
            PopularMovie(id = it, posterPath = "", releaseDate = "", title = "", rating = "", isFavorite = false)
        }.toList()
    }
}
