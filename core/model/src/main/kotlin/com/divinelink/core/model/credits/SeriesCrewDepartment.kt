package com.divinelink.core.model.credits

import com.divinelink.core.model.details.Person

data class SeriesCrewDepartment(
  val department: String,
  val crewList: List<Person>,
)
