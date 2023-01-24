package com.andreolas.movierama.base.data.remote.popular.service

import com.andreolas.movierama.base.communication.ApiConstants
import com.andreolas.movierama.base.communication.RestClient
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.base.data.remote.popular.dto.PopularResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProdMovieService @Inject constructor(
    private val restClient: RestClient,
) : MovieService {

    override suspend fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> {

        val response = restClient.get<PopularResponseApi>(
            ApiConstants.TMDB_URL + "movie/popular?" +
                "api_key=${request.apiKey}" + "&language=en-US&" +
                "page=${request.page}"
        )

        return flowOf(response)
    }
}
