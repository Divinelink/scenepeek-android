package com.divinelink.core.database.media.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.database.media.mapper.toPersistableMovie
import com.divinelink.core.database.media.mapper.toPersistableTV
import com.divinelink.core.database.media.model.PersistableMovie
import com.divinelink.core.database.media.model.PersistableTV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface MediaDao {

  @Query("SELECT * FROM movie")
  fun fetchFavoriteMovies(): Flow<List<PersistableMovie>>

  @Query("SELECT * FROM tv")
  fun fetchFavoriteTVSeries(): Flow<List<PersistableTV>>

  @Query("SELECT id FROM movie")
  fun fetchFavoriteMovieIds(): Flow<List<Int>>

  @Query("SELECT id FROM tv")
  fun fetchFavoriteTVIds(): Flow<List<Int>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteMovie(movie: PersistableMovie)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavoriteTV(tv: PersistableTV)

  @Query("DELETE FROM movie WHERE id=:id")
  suspend fun removeFavoriteMovie(id: Int)

  @Query("DELETE FROM tv WHERE id=:id")
  suspend fun removeFavoriteTV(id: Int)

  @Query("SELECT COUNT(*) FROM movie WHERE id = :id")
  suspend fun checkIfFavorite(id: Int): Int

  @Query("SELECT COUNT(*) FROM tv WHERE id = :id")
  suspend fun checkIfFavoriteTV(id: Int): Int
}

suspend fun MediaDao.insertFavoriteMedia(media: MediaItem) {
  when (media) {
    is MediaItem.Media.Movie -> insertFavoriteMovie(
      media.copy(isFavorite = true).toPersistableMovie(),
    )
    is MediaItem.Media.TV -> insertFavoriteTV(media.copy(isFavorite = true).toPersistableTV())
    is MediaItem.Person -> TODO()
    MediaItem.Unknown -> {
      // Do nothing
    }
  }
}

suspend fun MediaDao.removeFavoriteMedia(
  id: Int,
  mediaType: MediaType,
) {
  when (mediaType) {
    MediaType.MOVIE -> removeFavoriteMovie(id)
    MediaType.TV -> removeFavoriteTV(id)
    MediaType.PERSON -> TODO()
    MediaType.UNKNOWN -> {
      // Do nothing
    }
  }
}

suspend fun MediaDao.checkIfMediaIsFavorite(
  id: Int,
  mediaType: MediaType,
): Boolean = when (mediaType) {
  MediaType.MOVIE -> checkIfFavorite(id) > 0
  MediaType.TV -> checkIfFavoriteTV(id) > 0
  MediaType.PERSON -> TODO()
  MediaType.UNKNOWN -> false
}

fun MediaDao.fetchFavoriteMediaIDs(): Flow<List<Pair<Int, MediaType>>> {
  val movieIdsFlow = fetchFavoriteMovieIds()
  val tvIdsFlow = fetchFavoriteTVIds()

  return movieIdsFlow.combine(tvIdsFlow) { movieIds, tvIds ->
    val combinedList = mutableListOf<Pair<Int, MediaType>>()

    movieIds.forEach { id ->
      combinedList.add(id to MediaType.MOVIE)
    }

    tvIds.forEach {
      combinedList.add(it to MediaType.TV)
    }

    combinedList
  }
}
