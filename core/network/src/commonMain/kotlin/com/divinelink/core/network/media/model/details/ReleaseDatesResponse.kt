package com.divinelink.core.network.media.model.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseDatesResponse(
  val results: List<CountryReleaseResponse>,
)

@Serializable
data class CountryReleaseResponse(
  @SerialName("iso_3166_1") val countryCode: String,
  @SerialName("release_dates") val releases: List<ReleaseDetailResponse>,
)

@Serializable
data class ReleaseDetailResponse(
  val certification: String,
  val note: String,
  @SerialName("iso_639_1") val languageCode: String,
  @SerialName("release_date") val releaseDate: String,
  val type: Int,
)
