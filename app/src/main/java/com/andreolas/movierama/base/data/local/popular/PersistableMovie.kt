package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.divinelink.core.model.media.MediaItem

@Entity(tableName = "movie")
data class PersistableMovie(
  @PrimaryKey
  val id: Int,
  val title: String,
  val posterPath: String,
  val releaseDate: String,
  val rating: String,
  val isFavorite: Boolean,
  val overview: String,
)

internal fun MediaItem.Media.toPersistableMovie(): PersistableMovie {
  return PersistableMovie(
    id = this.id,
    title = this.name,
    posterPath = this.posterPath ?: "",
    releaseDate = this.releaseDate,
    rating = this.rating,
    isFavorite = this.isFavorite ?: false,
    overview = this.overview,
  )
}

fun List<PersistableMovie>.toDomainMoviesList(): List<MediaItem.Media> {
  return this.map(PersistableMovie::toMovie)
}

private fun PersistableMovie.toMovie(): MediaItem.Media.Movie {
  return MediaItem.Media.Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    name = this.title,
    rating = this.rating,
    overview = this.overview,
    isFavorite = this.isFavorite,
  )
}
