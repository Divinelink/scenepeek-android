package com.andreolas.movierama.base.data.remote.movies.service

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchResponseApi
import kotlinx.coroutines.flow.Flow

interface MovieService {

    fun fetchPopularMovies(
        request: PopularRequestApi,
    ): Flow<PopularResponseApi>

    fun fetchSearchMovies(
        request: SearchRequestApi,
    ): Flow<SearchResponseApi>

    fun fetchDetails(
        request: DetailsRequestApi,
    ): Flow<DetailsResponseApi>

    fun fetchReviews(
        request: ReviewsRequestApi,
    ): Flow<ReviewsResponseApi>

    fun fetchSimilarMovies(
        request: SimilarRequestApi,
    ): Flow<SimilarResponseApi>

    fun fetchVideos(
        request: VideosRequestApi,
    ): Flow<VideosResponseApi>
}
