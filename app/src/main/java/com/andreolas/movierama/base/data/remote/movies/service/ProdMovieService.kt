package com.andreolas.movierama.base.data.remote.movies.service

import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchResponseApi
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProdMovieService @Inject constructor(
    private val restClient: RestClient,
) : MovieService {

//    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//    private val apiKey = remoteConfig.getString("tmdb_api_key")
    private val apiKey = Firebase.remoteConfig.getString("tmdb_api_key")

    override suspend fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> {
        val baseUrl = "${ApiConstants.TMDB_URL}/movie/popular?"
        val url = baseUrl + "api_key=$apiKey&language=en-US&page=${request.page}"

        val response = restClient.get<PopularResponseApi>(
            url = url,
        )

        return flowOf(response)
    }

    override suspend fun fetchSearchMovies(request: SearchRequestApi): Flow<SearchResponseApi> {
        val baseUrl = "${ApiConstants.TMDB_URL}/search/movie?"
        val url = baseUrl + "api_key=$apiKey&language=en-US&page=${request.page}&include_adult=false"

        val response = restClient.get<SearchResponseApi>(
            url = url,
        )

        return flowOf(response)
    }
}
