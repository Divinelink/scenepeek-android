package com.divinelink.core.model.change

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ChangeValue

@Serializable
data class ProfileValue(val profile: ImageChange) : ChangeValue

@Serializable
data class BackdropValue(val backdrop: ImageChange) : ChangeValue

@Serializable
data class PosterValue(val poster: ImageChange) : ChangeValue

@Serializable
data class TitleLogoValue(@SerialName("title_logo") val titleLogo: ImageChange) : ChangeValue

@Serializable
data class StringValue(val value: String) : ChangeValue

@Serializable
data class PlotKeywordsValue(
  val name: String,
  val id: Long,
  val group: String,
) : ChangeValue

@Serializable
data class VideosValue(
  val id: String,
  val name: String,
  val key: String,
  val size: Int,
  val site: String,
  val type: String,
) : ChangeValue

@Serializable
data class CastValue(
  @SerialName("person_id") val personId: Long,
  val character: String,
  val order: Int,
  @SerialName("cast_id") val castId: Int,
  @SerialName("credit_id") val creditId: String,
) : ChangeValue

@Serializable
data class ReleaseDatesValue(
  val certification: String,
  @SerialName("iso_3166_1") val iso31661: String,
  @SerialName("iso_639_1") val iso6391: String,
  val note: String,
  @SerialName("release_date") val releaseDate: String,
  val type: Int,
) : ChangeValue

@Serializable
data class AlternativeTitlesValue(
  val title: String,
  val type: String,
  @SerialName("iso_3166_1") val iso31661: String,
) : ChangeValue

@Serializable
data class TaglineValue(
  val primary: Boolean,
  val tagline: String,
) : ChangeValue
