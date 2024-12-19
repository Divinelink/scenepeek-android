package com.divinelink.core.database.media.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv")
data class PersistableTV(
  @PrimaryKey
  val id: Int,
  val title: String,
  val posterPath: String,
  val releaseDate: String,
  val voteAverage: Double,
  val voteCount: Int,
  val isFavorite: Boolean,
  val overview: String,
)
