package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreolas.movierama.home.domain.model.MediaItem

@Entity(tableName = "tv")
data class PersistableTV(
  @PrimaryKey
  val id: Int,
  val title: String,
  val posterPath: String,
  val releaseDate: String,
  val rating: String,
  val isFavorite: Boolean,
  val overview: String,
)

internal fun MediaItem.Media.TV.toPersistableTV() = PersistableTV(
  id = this.id,
  title = this.name,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate,
  rating = this.rating,
  isFavorite = this.isFavorite ?: false,
  overview = this.overview,
)

internal fun List<PersistableTV>.map(): List<MediaItem.Media> = this.map(
  PersistableTV::toTV
)

internal fun PersistableTV.toTV() = MediaItem.Media.TV(
  id = this.id,
  name = this.title,
  posterPath = this.posterPath,
  releaseDate = this.releaseDate,
  rating = this.rating,
  overview = this.overview,
  isFavorite = this.isFavorite,
)
