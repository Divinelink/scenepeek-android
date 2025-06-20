package com.divinelink.core.model.details

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus

data class Season(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?,
  val airDate: String,
  val episodeCount: Int,
  val voteAverage: Double,
  val seasonNumber: Int,
  val status: JellyseerrMediaStatus? = null,
)

fun Season.canBeRequested(): Boolean = status != JellyseerrMediaStatus.AVAILABLE &&
  status != JellyseerrMediaStatus.PARTIALLY_AVAILABLE &&
  status != JellyseerrMediaStatus.PROCESSING &&
  status != JellyseerrMediaStatus.PENDING

fun Season.isAvailable(): Boolean = status == JellyseerrMediaStatus.AVAILABLE ||
  status == JellyseerrMediaStatus.PARTIALLY_AVAILABLE ||
  status == JellyseerrMediaStatus.PROCESSING ||
  status == JellyseerrMediaStatus.PENDING
