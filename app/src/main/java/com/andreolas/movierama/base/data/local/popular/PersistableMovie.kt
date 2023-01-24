package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class PersistableMovie(
    @PrimaryKey
    val id: String,
    val title: String,
    val releaseDate: String,
    val rating: Double,
    val isLiked: Boolean,
)
