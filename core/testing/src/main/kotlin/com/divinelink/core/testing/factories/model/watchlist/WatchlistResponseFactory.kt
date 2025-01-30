package com.divinelink.core.testing.factories.model.watchlist

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.watchlist.WatchlistResponse

object WatchlistResponseFactory {

  fun movies(
    page: Int = 1,
    canFetchMore: Boolean = true,
  ) = WatchlistResponse(
    data = MediaItemFactory.MoviesList(page * 1..page * 20),
    totalResults = 30,
    type = MediaType.MOVIE,
    canFetchMore = canFetchMore,
  )

  fun tv(
    page: Int = 1,
    canFetchMore: Boolean = true,
  ) = WatchlistResponse(
    data = MediaItemFactory.TVList(page * 10..page * 20),
    totalResults = 30,
    type = MediaType.TV,
    canFetchMore = canFetchMore,
  )

  fun emptyMovies() = WatchlistResponse(
    data = emptyList(),
    totalResults = 0,
    type = MediaType.MOVIE,
    canFetchMore = false,
  )

  fun emptyTV() = WatchlistResponse(
    data = emptyList(),
    totalResults = 0,
    type = MediaType.TV,
    canFetchMore = false,
  )

  fun all() = listOf(movies(), tv())
}
