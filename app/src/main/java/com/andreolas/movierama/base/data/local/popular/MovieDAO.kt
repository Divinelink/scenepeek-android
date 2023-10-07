package com.andreolas.movierama.base.data.local.popular

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

  @Query("SELECT * FROM movie UNION SELECT * FROM tv")
  fun fetchFavoriteMovies(): Flow<List<PersistableMovie>>

  @Query("SELECT id FROM movie")
  fun fetchFavoriteMoviesIds(): Flow<List<Int>>

  @Insert(
    onConflict = OnConflictStrategy.REPLACE,
  )
  suspend fun insertFavoriteMovie(
    movie: PersistableMovie,
  )

  @Query("DELETE FROM movie WHERE id=:id")
  suspend fun removeFavoriteMovie(
    id: Int,
  )

  @Query("SELECT COUNT(*) FROM movie WHERE id = :id")
  suspend fun checkIfFavorite(
    id: Int,
  ): Int
}
