package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.details.domain.model.Video
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow

/**
 * The data layer for any requests related to Movie Details Movies.
 */
interface DetailsRepository {

    fun fetchMovieDetails(
        request: DetailsRequestApi,
    ): Flow<Result<MovieDetails>>

    fun fetchMovieReviews(
        request: ReviewsRequestApi,
    ): Flow<Result<List<Review>>>

    fun fetchSimilarMovies(
        request: SimilarRequestApi,
    ): Flow<Result<List<SimilarMovie>>>

    fun fetchVideos(
        request: VideosRequestApi,
    ): Flow<Result<List<Video>>>
}
