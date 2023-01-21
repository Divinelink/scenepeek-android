package com.andreolas.movierama.base.data.remote.popular

import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.base.data.remote.popular.dto.PopularResponseApi
import kotlinx.coroutines.flow.Flow

interface MovieService {

    suspend fun fetchPopularMovies(
        request: PopularRequestApi,
    ): Flow<PopularResponseApi>

    companion object {
//        fun create(): MovieService {
//            return ProdMovieService(
//                client = HttpClient(Android) {
//                    install(Logging) {
//                        level = LogLevel.ALL
//                    }
//                    install(ContentNegotiation) {
//                        json(
//                            Json {
//                                prettyPrint = true
//                                isLenient = true
//                            }
//                        )
//                    }
//                }
//            )
//        }
    }
}
