package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

open class GetMovieDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MovieDetails>(dispatcher) {

    override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetails>> {
        val details = repository.fetchMovieDetails(
            request = parameters,
        )

        val reviews = repository.fetchMovieReviews(
            request = ReviewsRequestApi(
                movieId = parameters.movieId,
            )
        )

        return details.combineTransform(reviews) { details, reviews ->
            if (details is Result.Success && reviews is Result.Success) {
                val result = details.data.copy(reviews = reviews.data)
                emit(Result.Success(result))
            } else if (details is Result.Success) {
                emit(Result.Success(details.data))
            } else if (reviews is Result.Success) {
                emit(Result.Success(emptyMovieDetails.copy(reviews = reviews.data)))
            } else {
                emit(Result.Error(Exception("Something went wrong.")))
            }
        }
    }
}

private val emptyMovieDetails = MovieDetails(
    id = 0,
    title = "",
    posterPath = "",
    overview = null,
    genres = listOf(),
    director = null,
    cast = listOf(),
    releaseDate = "",
    rating = "",
    runtime = null,
    isFavorite = false,
    reviews = listOf(),
    similarMovies = listOf(),
)
