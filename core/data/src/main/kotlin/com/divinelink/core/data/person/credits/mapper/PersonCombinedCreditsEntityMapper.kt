package com.divinelink.core.data.person.credits.mapper

import com.divinelink.core.database.person.PersonCombinedCreditsEntity
import com.divinelink.core.model.person.credits.PersonCombinedCredits

fun PersonCombinedCreditsEntity.map() = PersonCombinedCredits(
  id = id,
  cast = cast.map { it.map() },
  crew = crew.map { it.map() },
)
