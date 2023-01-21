package com.andreolas.movierama.base.data.remote.popular

import com.andreolas.movierama.BuildConfig
import com.andreolas.movierama.base.data.remote.RestClient.client
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.base.data.remote.popular.dto.PopularResponseApi
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow

class ProdMovieService : MovieService {

    override suspend fun fetchPopularMovies(request: PopularRequestApi): Flow<PopularResponseApi> {

        return client.post(BuildConfig.TMDB_BASE_URL) {
            setBody(request)
        }.body()
    }
}
