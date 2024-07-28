package com.divinelink.core.model.details.person

import com.divinelink.core.model.details.Person

data class PersonDetails(
  val person: Person,
  val biography: String?,
  val birthday: String?,
  val deathday: String?,
  val placeOfBirth: String?,
  val homepage: String?,
  val alsoKnownAs: List<String>,
  val imdbId: String?,
  val popularity: Double,
)
