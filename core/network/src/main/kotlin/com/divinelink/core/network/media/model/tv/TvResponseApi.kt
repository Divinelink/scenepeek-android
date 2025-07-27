package com.divinelink.core.network.media.model.tv

import com.divinelink.core.commons.extensions.round
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvResponseApi(
  val page: Int,
  val results: List<TvItemApi>,
  @SerialName("total_pages") val totalPages: Int,
  @SerialName("total_results") val totalResults: Int,
)

@Serializable
data class TvItemApi(
  @SerialName("id") val id: Int,
  @SerialName("adult") val adult: Boolean,
  @SerialName("backdrop_path") val backdropPath: String? = null,
  @SerialName("genre_ids") val genreIds: List<Int>,
  @SerialName("origin_country") val originCountry: List<String>,
  @SerialName("original_language") val originalLanguage: String,
  @SerialName("original_name") val originalName: String,
  @SerialName("overview") val overview: String,
  @SerialName("popularity") val popularity: Double,
  @SerialName("poster_path") val posterPath: String? = null,
  @SerialName("first_air_date") val firstAirDate: String,
  @SerialName("name") val name: String,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Int?,
  @SerialName("rating") val rating: Double? = null,
)

fun TvResponseApi.map(): PaginationData<MediaItem.Media> = PaginationData(
  page = page,
  totalPages = totalPages,
  totalResults = totalResults,
  list = results.map(TvItemApi::toTv),
)

private fun TvItemApi.toTv() = MediaItem.Media.TV(
  id = this.id,
  posterPath = this.posterPath,
  backdropPath = this.backdropPath,
  releaseDate = this.firstAirDate,
  name = this.name,
  voteAverage = this.voteAverage.round(1),
  voteCount = this.voteCount ?: 0,
  overview = this.overview,
  isFavorite = false,
  accountRating = rating?.toInt(),
)
