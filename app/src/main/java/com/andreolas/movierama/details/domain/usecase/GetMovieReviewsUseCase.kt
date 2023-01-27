package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetMovieReviewsUseCase @Inject constructor(
    private val repository: DetailsRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<ReviewsRequestApi, List<Review>>(dispatcher) {

    override fun execute(parameters: ReviewsRequestApi): Flow<Result<List<Review>>> {
        val movieDetails = repository.fetchMovieReviews(
            request = parameters,
        )

        return movieDetails.map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                Result.Loading -> result
            }
        }
    }
}
