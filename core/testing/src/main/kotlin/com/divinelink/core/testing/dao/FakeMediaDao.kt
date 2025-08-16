package com.divinelink.core.testing.dao

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.model.PersistableMovie
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeMediaDao {

  val mock: MediaDao = mock()

  fun mockFetchFavoritesMovies(result: Flow<List<PersistableMovie>>) {
    whenever(
      mock.fetchFavoriteMovies(),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchFavoriteMovieIds(result: Flow<List<Int>>) {
    whenever(
      mock.fetchFavoriteMovieIds(),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchFavoriteTvIds(result: Flow<List<Int>>) {
    whenever(
      mock.fetchFavoriteTVIds(),
    ).thenReturn(
      result,
    )
  }

  suspend fun verifyInsertFavoriteMovie(movie: PersistableMovie) {
    verify(mock).insertFavoriteMovie(movie)
  }

  suspend fun verifyRemoveMovie(id: Int) {
    verify(mock).removeFavoriteMovie(id)
  }

  suspend fun mockCheckIfFavorite(
    id: Int,
    result: Int,
  ) {
    whenever(mock.checkIfFavorite(id)).thenReturn(result)
  }

  suspend fun mockNoFavorites() {
    whenever(mock.checkIfFavorite(any())).thenReturn(0)
  }

  suspend fun mockNoTvFavorites() {
    whenever(mock.checkIfFavoriteTV(any())).thenReturn(0)
  }
}
