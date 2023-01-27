package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.toDomainReviewsList
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.toDomainMoviesList
import com.andreolas.movierama.base.data.remote.movies.dto.details.toDomainMovie
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdDetailsRepository @Inject constructor(
    private val movieRemote: MovieService,
) : DetailsRepository {

    override fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MovieDetails>> {
        return movieRemote
            .fetchDetails(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMovie())
            }.catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }

    override fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>> {
        return movieRemote
            .fetchReviews(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainReviewsList())
            }.catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }

    override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<Result<List<SimilarMovie>>> {
        return movieRemote
            .fetchSimilarMovies(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMoviesList())
            }.catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }
}
