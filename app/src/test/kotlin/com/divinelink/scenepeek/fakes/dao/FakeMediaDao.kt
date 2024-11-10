package com.divinelink.scenepeek.fakes.dao

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.model.PersistableMovie
import kotlinx.coroutines.flow.Flow
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
}
