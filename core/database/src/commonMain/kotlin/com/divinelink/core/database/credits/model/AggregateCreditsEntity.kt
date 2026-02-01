package com.divinelink.core.database.credits.model

import com.divinelink.core.model.details.Person

data class AggregateCreditsEntity(
  val id: Long,
  val crew: List<Person>,
  val cast: List<Person>,
)
