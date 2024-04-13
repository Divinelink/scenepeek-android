package com.andreolas.movierama.base.data.remote.movies.dto.details.similar

import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import gr.divinelink.core.util.extensions.round
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarResponseApi(
  val page: Int,
  val results: List<SimilarMovieApi>,
  @SerialName("total_pages")
  val totalPages: Int,
  @SerialName("total_results")
  val totalResults: Int,
)

@Serializable
data class SimilarMovieApi(
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

internal fun SimilarResponseApi.toDomainMoviesList(mediaType: MediaType): List<MediaItem.Media> {
  return this.results.map { it.toMediaItem(mediaType) }
}

private fun SimilarMovieApi.toMediaItem(mediaType: MediaType): MediaItem.Media = when (mediaType) {
  MediaType.MOVIE -> MediaItem.Media.Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    name = this.title,
    rating = this.voteAverage.round(1).toString(),
    overview = this.overview,
    isFavorite = null,
  )
  MediaType.TV -> MediaItem.Media.TV(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    name = this.title,
    rating = this.voteAverage.round(1).toString(),
    overview = this.overview,
    isFavorite = null,
  )
  else -> {
    throw IllegalArgumentException("Unknown media value")
  }
}
