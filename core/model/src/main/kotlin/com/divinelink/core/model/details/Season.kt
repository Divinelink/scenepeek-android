package com.divinelink.core.model.details

import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus

data class Season(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?,
  val airDate: String,
  val episodeCount: Int,
  val voteAverage: Double,
  val seasonNumber: Int,
  val status: JellyseerrStatus.Media? = null,
)

fun Season.canBeRequested(): Boolean = status != JellyseerrStatus.Media.AVAILABLE &&
  status != JellyseerrStatus.Media.PARTIALLY_AVAILABLE &&
  status != JellyseerrStatus.Media.PROCESSING &&
  status != JellyseerrStatus.Media.PENDING

fun Season.isAvailable(): Boolean = status == JellyseerrStatus.Media.AVAILABLE ||
  status == JellyseerrStatus.Media.PARTIALLY_AVAILABLE ||
  status == JellyseerrStatus.Media.PROCESSING ||
  status == JellyseerrStatus.Media.PENDING
