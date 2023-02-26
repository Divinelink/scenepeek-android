package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

open class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<PopularRequestApi, List<PopularMovie>>(dispatcher) {
    override fun execute(
        parameters: PopularRequestApi,
    ): Flow<MoviesListResult> {
        val favoriteMoviesIdsFlow = moviesRepository.fetchFavoriteMoviesIds()
        val popularMoviesFlow = moviesRepository.fetchPopularMovies(parameters)

        return combine(favoriteMoviesIdsFlow, popularMoviesFlow) { favorite, popular ->
            when {
                favorite is Result.Success && popular is Result.Success -> {
                    Result.Success(
                        getMoviesWithUpdatedFavoriteStatus(
                            favoriteIds = favorite,
                            popular = popular,
                        )
                    )
                }

                popular is Result.Success -> Result.Success(popular.data)
                favorite is Result.Error -> favorite
                popular is Result.Error -> popular
                else -> Result.Loading
            }
        }
    }
}

/**
 *  @param [favoriteIds] ids are fetched through local data.
 *  @param [popular] movies are fetched through api call.
 *
 * The method filters the favorite list to get all the movies that have the isFavorite property set to true and then it iterates over
 * the popular list and for each movie, it checks if there is a corresponding movie in the favorite list with the same id, if yes
 * it creates a copy of the movie with the isFavorite = true otherwise it returns the same movie.
 * The returned list is a combination of both favorite and popular movies but with the same id movies,
 * the one with isFavorite = true will be present in the list.
 */

fun getMoviesWithUpdatedFavoriteStatus(
    favoriteIds: Result.Success<List<Int>>,
    popular: Result.Success<List<PopularMovie>>,
): List<PopularMovie> {
    return popular.data.map { popularMovie ->
        favoriteIds.data.find { id -> id == popularMovie.id }?.let {
            popularMovie.copy(isFavorite = true)
        } ?: popularMovie
    }
}
