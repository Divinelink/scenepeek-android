package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
            //            emit(Result.Loading)
            coroutineScope {
                val result = withContext(dispatcher) {
                    moviesRepository.fetchPopularMovies(parameters)
                }
                emit(result.first())
            }
        }

        return favoriteMovies.combineTransform(popularMovies) { favorite, popular ->
            if (favorite is Result.Success && popular is Result.Success) {

                val mergedList = sanitizeMovies(favorite, popular)
                emit(Result.Success(mergedList))
            } else if (popular is Result.Success) {
                emit(Result.Success(popular.data))
            } else {
                emit(Result.Error(Exception("Something went wrong.")))
            }
        }
    }

    /**
     *  @param [favorite] movies are fetched through local data.
     *  @param [popular] movies are fetched through api call.
     *
     * Combine the movies with the same ID but keep those with isFavorite is true and keep
     * the rest with isFavorite is false.
     * This will results in a [mergedList] with both favorite and non favorite movies,
     * but it'll only contain distinct movies.
     *
     * It's worth noting that we should add the favorite items into the popular items list and not the other way around.
     * That way we preserve the order that the items were fetched with.
     *
     */
    private fun sanitizeMovies(
        favorite: Result.Success<List<PopularMovie>>,
        popular: Result.Success<List<PopularMovie>>,
    ): List<PopularMovie> {
        val mergedList = (popular.data + favorite.data)
            .groupBy { it.id }.map { (_, group) ->
                group.firstOrNull { it.isFavorite } ?: group.first()
            }
        return mergedList
    }
}
