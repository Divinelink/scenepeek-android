package com.andreolas.movierama.base.data.remote.movies.dto.popular

import com.andreolas.movierama.home.domain.model.PopularMovie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularResponseApi(
    val page: Int,
    val results: List<PopularMovieApi>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)

@Serializable
data class PopularMovieApi(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int?,
)

internal fun PopularResponseApi.toDomainMoviesList(): List<PopularMovie> {
    return this.results.map(PopularMovieApi::toPopularMovie)
}

private fun PopularMovieApi.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.posterPath ?: "",
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.voteAverage.toString(),
        isFavorite = false,
    )
}
