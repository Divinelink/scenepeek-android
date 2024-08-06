package com.divinelink.core.network.details.person.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonCreditsApi(
  val id: Long,
  val cast: List<PersonCastCreditApi>,
  val crew: List<PersonCrewCreditApi>,
)
