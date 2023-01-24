package com.andreolas.movierama.base.data.local.popular

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreolas.movierama.home.domain.model.PopularMovie

@Entity(tableName = "movie")
data class PersistableMovie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: String,
    val isFavorite: Boolean,
)

internal fun PopularMovie.toPersistableMovie(): PersistableMovie {
    return PersistableMovie(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        rating = this.rating,
        isFavorite = this.isFavorite
    )
}

internal fun List<PersistableMovie>.toDomainMoviesList(): List<PopularMovie> {
    return this.map(PersistableMovie::toPopularMovie)
}

private fun PersistableMovie.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.rating,
        isFavorite = this.isFavorite
    )
}
