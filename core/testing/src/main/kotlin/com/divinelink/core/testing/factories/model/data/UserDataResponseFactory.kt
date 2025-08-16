package com.divinelink.core.testing.factories.model.data

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataResponse

object UserDataResponseFactory {

  fun movies(
    page: Int = 1,
    canFetchMore: Boolean = true,
  ) = UserDataResponse(
    data = MediaItemFactory.MoviesList(page * 1..page * 20),
    totalResults = 30,
    type = MediaType.MOVIE,
    canFetchMore = canFetchMore,
    page = page,
  )

  fun tv(
    page: Int = 1,
    canFetchMore: Boolean = true,
  ) = UserDataResponse(
    data = MediaItemFactory.TVList(page * 10..page * 20),
    totalResults = 30,
    type = MediaType.TV,
    canFetchMore = canFetchMore,
    page = page,
  )

  fun emptyMovies() = UserDataResponse(
    data = emptyList(),
    totalResults = 0,
    type = MediaType.MOVIE,
    canFetchMore = false,
    page = 0,
  )

  fun emptyTV() = UserDataResponse(
    data = emptyList(),
    totalResults = 0,
    type = MediaType.TV,
    canFetchMore = false,
    page = 0,
  )

  fun all() = listOf(
    movies(),
    tv(),
  )
}
