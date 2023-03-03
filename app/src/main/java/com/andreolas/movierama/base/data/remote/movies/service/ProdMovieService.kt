package com.andreolas.movierama.base.data.remote.movies.service

import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.base.communication.RestClient
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
import com.andreolas.movierama.base.storage.EncryptedStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdMovieService @Inject constructor(
    private val restClient: RestClient,
    encryptedStorage: EncryptedStorage,
) : MovieService {
    private val apiKey = encryptedStorage.tmdbApiKey

    override fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> = flow {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/popular?"
        val url = baseUrl + "api_key=$apiKey&language=en-US&page=${request.page}"

        val response = restClient.get<PopularResponseApi>(
            url = url,
        )

        emit(response)
    }

    override fun fetchSearchMovies(request: SearchRequestApi): Flow<SearchResponseApi> = flow {
        val baseUrl = "${ApiConstants.TMDB_URL}/search/movie?"
        val url = baseUrl + "api_key=$apiKey" +
            "&language=en-US" +
            "&query=${request.query}" +
            "&page=${request.page}" +
            "&include_adult=false"

        val response = restClient.get<SearchResponseApi>(
            url = url,
        )

        emit(response)
    }

    override fun fetchDetails(request: DetailsRequestApi): Flow<DetailsResponseApi> = flow {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/"
        val url = baseUrl +
            "${request.movieId}" +
            "?api_key=$apiKey" +
            "&append_to_response=credits" +
            "&language=en-US"

        val response = restClient.get<DetailsResponseApi>(
            url = url,
        )

        emit(response)
    }

    override fun fetchReviews(request: ReviewsRequestApi): Flow<ReviewsResponseApi> = flow {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/"
        val url = baseUrl +
            "${request.movieId}" +
            "/reviews" +
            "?api_key=$apiKey" +
            "&language=en-US"

        val response = restClient.get<ReviewsResponseApi>(
            url = url,
        )

        emit(response)
    }

    override fun fetchSimilarMovies(request: SimilarRequestApi): Flow<SimilarResponseApi> = flow {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/"
        val url = baseUrl +
            "${request.movieId}" +
            "/similar" +
            "?api_key=$apiKey" +
            "&language=en-US"

        val response = restClient.get<SimilarResponseApi>(
            url = url,
        )

        emit(response)
    }

    override fun fetchVideos(request: VideosRequestApi): Flow<VideosResponseApi> {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/"
        val url = baseUrl +
            "${request.movieId}" +
            "/videos" +
            "?api_key=$apiKey" +
            "&language=en-US"

        return flow {
            val response = restClient.get<VideosResponseApi>(
                url = url,
            )

            emit(response)
        }
    }
}
