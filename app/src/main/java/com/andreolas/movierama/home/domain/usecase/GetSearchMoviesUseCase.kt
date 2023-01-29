package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

data class SearchResult(
    val query: String,
    val searchList: List<PopularMovie>,
)

class GetSearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequestApi, SearchResult>(dispatcher) {
    override fun execute(
        parameters: SearchRequestApi,
    ): Flow<Result<SearchResult>> {
        val favoriteMovies = moviesRepository.fetchFavoriteMovies()
        val searchMovies = moviesRepository.fetchSearchMovies(parameters)

        return favoriteMovies
            .distinctUntilChanged()
            .combineTransform(searchMovies) { favorite, search ->
                emit(Result.Loading)
                if (favorite is Result.Success && search is Result.Success) {
                    val searchWithFavorites = getFavoritesAndPopularMoviesCombined(
                        Result.Success(favorite.data),
                        Result.Success(search.data)
                    )
                    emit(
                        Result.Success(
                            SearchResult(
                                query = parameters.query,
                                searchList = searchWithFavorites,
                            )
                        )
                    )
                } else if (search is Result.Error) {
                    emit(Result.Error(Exception("Something went wrong.")))
                }
            }.conflate()
    }
}
