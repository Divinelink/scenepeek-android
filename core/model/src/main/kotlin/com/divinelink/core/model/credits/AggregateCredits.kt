package com.divinelink.core.model.credits

import com.divinelink.core.model.details.Person

data class AggregateCredits(
  val cast: List<Person>,
  val crewDepartments: List<SeriesCrewDepartment>,
  val id: Long,
)
