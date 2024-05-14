package com.divinelink.core.network.movies.model.popular

import com.divinelink.core.model.media.MediaItem
import gr.divinelink.core.util.extensions.round
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

fun PopularResponseApi.toDomainMoviesList(): List<MediaItem.Media.Movie> {
  return this.results.map(PopularMovieApi::toPopularMovie)
}

private fun PopularMovieApi.toPopularMovie() = MediaItem.Media.Movie(
  id = this.id,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate,
  name = this.title,
  rating = this.voteAverage.round(1).toString(),
  overview = this.overview,
  isFavorite = false,
)
