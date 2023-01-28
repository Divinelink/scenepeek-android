package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SearchResult(
    val combinedList: List<PopularMovie>,
    val searchMovies: List<PopularMovie>,
)

class GetSearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequestApi, SearchResult>(dispatcher) {
    override fun execute(
        parameters: SearchRequestApi,
    ): Flow<Result<SearchResult>> {
        val favoriteMovies = moviesRepository.fetchFavoriteMovies()
        val searchMovies = flow {
            coroutineScope {
                emit(Result.Loading)
                val result = withContext(dispatcher) {
                    moviesRepository.fetchSearchMovies(parameters)
                }
                emitAll(result)
            }
        }.flowOn(dispatcher)

        return favoriteMovies.combineTransform(searchMovies) { favorite, search ->
            if (favorite is Result.Success && search is Result.Success) {
                val mergedList = getFavoritesAndPopularMoviesCombined(
                    Result.Success(favorite.data),
                    Result.Success(search.data)
                )
                emit(Result.Success(SearchResult(combinedList = mergedList, searchMovies = search.data)))
            } else if (search is Result.Success) {
                // todo
            } else if (search is Result.Error) {
                emit(Result.Error(Exception("Something went wrong.")))
            }
        }
    }
}
