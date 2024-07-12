package com.divinelink.core.model.credits

data class AggregateCredits(
  val cast: List<SeriesCast>,
  val crewDepartments: List<SeriesCrewDepartment>,
  val id: Int,
)

