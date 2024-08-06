package com.divinelink.core.model.person.credits

data class PersonCombinedCredits(
  val id: Long,
  val cast: List<PersonCredit>,
  val crew: List<PersonCredit>,
)
