package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.model.Search

@Entity(tableName = "movie") // FIXME rename to media or smt.
data class PersistableMovie(
  @PrimaryKey
  val id: Int,
  val title: String,
  val posterPath: String,
  val releaseDate: String,
  val rating: String,
  val isFavorite: Boolean,
  val overview: String,
  val mediaType: String,
)

internal fun PopularMovie.toPersistableMovie(): PersistableMovie {
  return PersistableMovie(
    id = this.id,
    title = this.title,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    rating = this.rating,
    isFavorite = this.isFavorite,
    overview = this.overview,
    mediaType = "movie", // FIXME update this when we add tv shows
  )
}

internal fun List<PersistableMovie>.toDomainMoviesList(): List<Search.Media> {
  return this.map(PersistableMovie::toMovie)
}

private fun PersistableMovie.toPopularMovie(): PopularMovie {
  return PopularMovie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.title,
    rating = this.rating,
    isFavorite = this.isFavorite,
    overview = this.overview,
  )
}

private fun PersistableMovie.toMovie(): Search.Media.Movie {
  return Search.Media.Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    name = this.title,
    rating = this.rating,
    overview = this.overview,
    isFavorite = this.isFavorite,
  )
}
