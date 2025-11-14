package com.divinelink.core.model.details

import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import kotlinx.serialization.Serializable

@Serializable
data class Season(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?,
  val airDate: String,
  val episodeCount: Int,
  val voteAverage: Double,
  val seasonNumber: Int,
  val status: JellyseerrStatus? = null,
)

fun Season.isAvailable(): Boolean = status == JellyseerrStatus.Media.AVAILABLE ||
  status == JellyseerrStatus.Media.PARTIALLY_AVAILABLE ||
  status == JellyseerrStatus.Media.PROCESSING ||
  status == JellyseerrStatus.Media.PENDING ||
  status == JellyseerrStatus.Request.PENDING ||
  status == JellyseerrStatus.Request.FAILED ||
  status == JellyseerrStatus.Request.APPROVED ||
  status == JellyseerrStatus.Season.PENDING ||
  status == JellyseerrStatus.Season.PROCESSING

fun Season.canBeRequested(): Boolean = !isAvailable()
