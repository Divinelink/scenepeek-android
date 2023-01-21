package com.andreolas.movierama.base.data.network.popular

import kotlinx.coroutines.flow.Flow

interface MovieRemote {

    fun fetchPopularMovies(
        page: Int,
    ): Flow<List<ApiPopularResponse>>
}
