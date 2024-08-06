package com.divinelink.core.database.person

import com.divinelink.core.database.person.credits.PersonCastCreditEntity
import com.divinelink.core.database.person.credits.PersonCrewCreditEntity

data class PersonCombinedCreditsEntity(
  val id: Long,
  val cast: List<PersonCastCreditEntity>,
  val crew: List<PersonCrewCreditEntity>,
)
