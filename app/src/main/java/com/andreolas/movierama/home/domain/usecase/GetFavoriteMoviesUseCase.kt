package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetFavoriteMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<PopularMovie>>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<PopularMovie>>> {
        val favoriteMovies = moviesRepository.fetchFavoriteMovies()

        return favoriteMovies.map { result ->
            if (result is Result.Success) {
                Result.Success(result.data)
            } else {
                Result.Error(Exception("Something went wrong."))
            }
        }
    }
}
