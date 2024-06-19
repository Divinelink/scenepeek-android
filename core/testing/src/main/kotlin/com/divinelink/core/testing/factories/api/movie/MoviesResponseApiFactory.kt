package com.divinelink.core.testing.factories.api.movie

import com.divinelink.core.network.media.model.movie.MoviesResponseApi

object MoviesResponseApiFactory {

  fun full() = MoviesResponseApi(
    page = 1,
    results = MovieApiFactory.EmptyList(),
    totalPages = 3,
    totalResults = 60
  )
}
