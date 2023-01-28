package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<PopularRequestApi, List<PopularMovie>>(dispatcher) {
    override fun execute(
        parameters: PopularRequestApi,
    ): Flow<MoviesListResult> {
        val favoriteMovies = moviesRepository.fetchFavoriteMovies()
        val popularMovies = flow {
            coroutineScope {
                emit(Result.Loading)
                val result = withContext(dispatcher) {
                    moviesRepository.fetchPopularMovies(parameters)
                }
                emitAll(result)
            }
        }.flowOn(dispatcher)

        return favoriteMovies.combineTransform(popularMovies) { favorite, popular ->
            if (favorite is Result.Success && popular is Result.Success) {
                val mergedList = getFavoritesAndPopularMoviesCombined(favorite, popular)
                emit(Result.Success(mergedList))
            } else if (popular is Result.Success) {
                emit(Result.Success(popular.data))
            } else {
                emit(Result.Error(Exception("Something went wrong.")))
            }
        }
    }
}

/**
 *  @param [favorite] movies are fetched through local data.
 *  @param [popular] movies are fetched through api call.
 *
 * The method filters the favorite list to get all the movies that have the isFavorite property set to true and then it iterates over
 * the popular list and for each movie, it checks if there is a corresponding movie in the favorite list with the same id, if yes
 * it creates a copy of the movie with the isFavorite = true otherwise it returns the same movie.
 * The returned list is a combination of both favorite and popular movies but with the same id movies,
 * the one with isFavorite = true will be present in the list.
 */
fun getFavoritesAndPopularMoviesCombined(
    favorite: Result.Success<List<PopularMovie>>,
    popular: Result.Success<List<PopularMovie>>,
): List<PopularMovie> {
    return popular.data.map { popularMovie ->
        favorite.data.find { favorite -> favorite.id == popularMovie.id }?.let {
            popularMovie.copy(isFavorite = true)
        } ?: popularMovie
    }
}
