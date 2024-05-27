package com.divinelink.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
