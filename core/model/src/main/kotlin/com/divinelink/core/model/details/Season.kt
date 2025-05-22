package com.divinelink.core.model.details

data class Season(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?,
  val airDate: String,
  val episodeCount: Int,
  val voteAverage: Double,
  val seasonNumber: Int,
)
