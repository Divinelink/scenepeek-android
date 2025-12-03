package com.divinelink.core.database.credits.model

data class AggregateCreditsEntity(
  val id: Long,
  val crew: List<CrewEntity>,
  val cast: List<CastEntity>,
)
