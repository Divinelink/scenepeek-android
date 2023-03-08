package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow

class FakeDetailsRepository : DetailsRepository {

    override fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MovieDetails>> {
        TODO("Not yet implemented")
    }

    override fun fetchMovieReviews(request: ReviewsRequestApi): Flow<Result<List<Review>>> {
        TODO("Not yet implemented")
    }

    override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<Result<List<SimilarMovie>>> {
        TODO("Not yet implemented")
    }

    override fun fetchVideos(request: VideosRequestApi): Flow<Result<List<Video>>> {
        TODO("Not yet implemented")
    }
}
