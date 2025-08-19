package com.divinelink.core.testing.dao

import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TestMediaDao {

  val mock: MediaDao = mock()

  fun verifyItemInserted(item: MediaItem.Media) {
    verify(mock).insertMedia(item)
  }

  fun mockFetchFavorites(result: Flow<List<MediaItem.Media>>) {
    whenever(
      mock.fetchAllFavorites(),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchFavoriteMovieIds(result: Flow<List<Int>>) {
    whenever(
      mock.getFavoriteMediaIds(MediaType.MOVIE),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchFavoriteTvIds(result: Flow<List<Int>>) {
    whenever(
      mock.getFavoriteMediaIds(MediaType.TV),
    ).thenReturn(
      result,
    )
  }

  fun verifyInsertFavoriteMovie(id: Int) {
    verify(mock).addToFavorites(
      mediaId = id,
      mediaType = MediaType.MOVIE,
    )
  }

  fun verifyInsertFavoriteTV(id: Int) {
    verify(mock).addToFavorites(
      mediaId = id,
      mediaType = MediaType.TV,
    )
  }

  fun verifyRemoveMovie(id: Int) {
    verify(mock).removeFromFavorites(
      mediaId = id,
      mediaType = MediaType.MOVIE,
    )
  }

  fun mockCheckIfFavorite(
    id: Int,
    mediaType: MediaType,
    result: Boolean,
  ) {
    whenever(mock.isMediaFavorite(id, mediaType)).thenReturn(result)
  }
}
