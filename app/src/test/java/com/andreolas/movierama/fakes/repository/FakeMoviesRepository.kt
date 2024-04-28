package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.repository.MediaListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMoviesRepository {

  val mock: MoviesRepository = mock()

  fun mockFetchFavoriteMovies(
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchFavoriteMovies()
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchFavoriteTVSeries(
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchFavoriteTVSeries()
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchFavoriteMoviesIds(
    response: Result<List<Pair<Int, MediaType>>>,
  ) {
    whenever(
      mock.fetchFavoriteIds()
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchPopularMovies(
    request: PopularRequestApi,
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchPopularMovies(request)
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchSearchMovies(
    request: SearchRequestApi,
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchSearchMovies(request)
    ).thenReturn(
      flowOf(response)
    )
  }

  suspend fun mockMarkAsFavorite(
    media: MediaItem.Media,
    response: Result<Unit>,
  ) {
    whenever(
      mock.insertFavoriteMedia(media)
    ).thenReturn(
      response
    )
  }

  suspend fun mockCheckFavorite(
    id: Int,
    mediaType: MediaType,
    response: Result<Boolean>,
  ) {
    whenever(
      mock.checkIfMediaIsFavorite(id, mediaType)
    ).thenReturn(response)
  }

  suspend fun verifyCheckIsFavorite(
    response: Result<Boolean>,
  ) {
    whenever(
      mock.checkIfMediaIsFavorite(any(), any())
    ).thenReturn(response)
  }

  suspend fun mockRemoveFavorite(
    id: Int,
    mediaType: MediaType,
    response: Result<Unit>,
  ) {
    whenever(
      mock.removeFavoriteMedia(id, mediaType)
    ).thenReturn(
      response
    )
  }
}
