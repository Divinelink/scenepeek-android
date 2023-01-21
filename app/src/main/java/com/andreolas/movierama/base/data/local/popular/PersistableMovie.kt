package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class PersistableMovie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: String,
    val isFavorite: Boolean,
)
