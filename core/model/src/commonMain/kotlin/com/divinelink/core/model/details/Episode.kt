package com.divinelink.core.model.details

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
  val id: Int,
  val name: String,
  val airDate: String?,
  val overview: String?,
  val runtime: String?,
  val number: Int,
  val seasonNumber: Int,
  val showId: Int,
  val stillPath: String?,
  val voteAverage: String?,
  val voteCount: Int?,
  val crew: List<Person>,
  val guestStars: List<Person>,
  val accountRating: Int?,
)
