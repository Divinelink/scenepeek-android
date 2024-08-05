package com.divinelink.core.network.details.person.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonCombinedCreditsApi(
  val id: Int,
  val cast: List<PersonCastCreditApi>,
  val crew: List<PersonCrewCreditApi>,
)
