package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject

open class GetMovieDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MovieDetailsResult>(dispatcher) {

    override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetailsResult>> {
        val details = repository.fetchMovieDetails(
            request = parameters,
        )

        val reviews = repository.fetchMovieReviews(
            request = ReviewsRequestApi(
                movieId = parameters.movieId,
            )
        )

        val similar = repository.fetchSimilarMovies(
            request = SimilarRequestApi(
                movieId = parameters.movieId,
            )
        )
        return combineTransform(
            flow = details,
            flow2 = reviews,
            flow3 = similar,
        ) { detailsFlow, reviewsFlow, similarFlow ->
            when (detailsFlow) {
                Result.Loading -> emit(Result.Loading)
                is Result.Error -> throw MovieDetailsException()
                is Result.Success -> emit(Result.Success(MovieDetailsResult.DetailsSuccess(detailsFlow.data)))
            }
            if (reviewsFlow is Result.Success) {
                emit(Result.Success(MovieDetailsResult.ReviewsSuccess(reviews = reviewsFlow.data)))
            }

            if (similarFlow is Result.Success) {
                emit(Result.Success(MovieDetailsResult.SimilarSuccess(similar = similarFlow.data)))
            }
        }
    }
}
