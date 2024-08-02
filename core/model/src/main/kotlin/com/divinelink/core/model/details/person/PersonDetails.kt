package com.divinelink.core.model.details.person

import com.divinelink.core.commons.extensions.calculateAge
import com.divinelink.core.model.details.Person

data class PersonDetails(
  val person: Person,
  val biography: String?,
  val birthday: String?,
  val deathday: String?,
  val knownForDepartment: String?,
  val placeOfBirth: String?,
  val homepage: String?,
  val alsoKnownAs: List<String>,
  val imdbId: String?,
  val popularity: Double,
) {
  val currentAge = birthday?.let { birthday -> calculateAge(birthday) }
  val ageAtDeath = deathday?.let { deathday ->
    birthday?.let { calculateAge(birthday, deathday) }
  }
  val isAlive = deathday == null && currentAge != null
}
