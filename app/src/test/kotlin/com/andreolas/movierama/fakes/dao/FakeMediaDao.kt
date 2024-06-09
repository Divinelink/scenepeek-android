package com.andreolas.movierama.fakes.dao

import com.divinelink.database.dao.MediaDao
import com.divinelink.database.model.PersistableMovie
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeMediaDao {

  val mock: com.divinelink.database.dao.MediaDao = mock()

  fun mockFetchFavoritesMovies(
    result: Flow<List<com.divinelink.database.model.PersistableMovie>>,
  ) {
    whenever(
      mock.fetchFavoriteMovies()
    ).thenReturn(
      result
    )
  }

  suspend fun verifyInsertFavoriteMovie(
    movie: com.divinelink.database.model.PersistableMovie,
  ) {
    verify(mock).insertFavoriteMovie(movie)
  }

  suspend fun verifyRemoveMovie(
    id: Int,
  ) {
    verify(mock).removeFavoriteMovie(id)
  }

  suspend fun mockCheckIfFavorite(
    id: Int,
    result: Int,
  ) {
    whenever(mock.checkIfFavorite(id)).thenReturn(result)
  }
}
