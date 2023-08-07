package com.andreolas.movierama.base.data.remote.movies.dto.search.movie

import com.andreolas.movierama.home.domain.model.MediaItem
import gr.divinelink.core.util.extensions.round
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseApi(
  val page: Int,
  val results: List<SearchMovieApi>,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
)

@Serializable
data class SearchMovieApi(
  val id: Int,
  val adult: Boolean,
  @SerialName("backdrop_path") val backdropPath: String? = null,
  @SerialName("genre_ids") val genreIds: List<Int>,
  @SerialName("original_language") val originalLanguage: String,
  @SerialName("original_title") val originalTitle: String,
  val overview: String,
  val popularity: Double,
  @SerialName("poster_path") val posterPath: String? = null,
  @SerialName("release_date") val releaseDate: String? = null,
  val title: String,
  val video: Boolean,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Int?,
)

internal fun SearchResponseApi.toDomainMoviesList(): List<MediaItem.Media.Movie> {
  return this.results.map(SearchMovieApi::toMovieMediaItem)
}

private fun SearchMovieApi.toMovieMediaItem(): MediaItem.Media.Movie {
  return MediaItem.Media.Movie(
    id = this.id,
    posterPath = this.posterPath ?: "",
    releaseDate = this.releaseDate ?: "",
    name = this.title,
    rating = this.voteAverage.round(1).toString(),
    overview = this.overview,
    isFavorite = false,
  )
}
