package com.andreolas.movierama.base.data.remote.movies.service

import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchResponseApi
import com.andreolas.movierama.base.storage.EncryptedPreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProdMovieService @Inject constructor(
    private val restClient: RestClient,
    encryptedStorage: EncryptedPreferenceStorage,
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
            "${request.movieId}?append_to_response=credits" +
            "api_key=$apiKey" +
            "&language=en-US"

        val response = restClient.get<DetailsResponseApi>(
            url = url,
        )

        emit(response)
    }
}
