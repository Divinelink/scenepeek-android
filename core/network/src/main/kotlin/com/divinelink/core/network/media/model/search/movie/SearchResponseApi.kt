package com.divinelink.core.network.media.model.search.movie

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.media.MediaItem
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

fun SearchResponseApi.toDomainMoviesList(): List<MediaItem.Media.Movie> = this
  .results
  .map(SearchMovieApi::toMovieMediaItem)

private fun SearchMovieApi.toMovieMediaItem() = MediaItem.Media.Movie(
  id = this.id,
  posterPath = this.posterPath ?: "",
  releaseDate = this.releaseDate ?: "",
  name = this.title,
  voteAverage = this.voteAverage.round(1),
  voteCount = this.voteCount ?: 0,
  overview = this.overview,
  isFavorite = false,
)
