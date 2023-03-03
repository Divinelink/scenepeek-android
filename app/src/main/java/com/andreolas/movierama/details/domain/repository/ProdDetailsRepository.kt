package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.toDomainReviewsList
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.toDomainMoviesList
import com.andreolas.movierama.base.data.remote.movies.dto.details.toDomainMovie
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.toDomainVideosList
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.ReviewsException
import com.andreolas.movierama.details.domain.model.SimilarException
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.VideosException
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
            }.catch {
                throw MovieDetailsException()
            }
    }

    override fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>> {
        return movieRemote
            .fetchReviews(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainReviewsList())
            }.catch {
                throw ReviewsException()
            }
    }

    override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<Result<List<SimilarMovie>>> {
        return movieRemote
            .fetchSimilarMovies(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMoviesList())
            }.catch {
                throw SimilarException()
            }
    }

    override fun fetchVideos(request: VideosRequestApi): Flow<Result<List<Video>>> {
        return movieRemote
            .fetchVideos(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainVideosList())
            }.catch {
                throw VideosException()
            }
    }
}
