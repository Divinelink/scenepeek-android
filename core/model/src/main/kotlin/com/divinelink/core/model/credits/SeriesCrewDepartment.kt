package com.divinelink.core.model.credits

data class SeriesCrewDepartment(
  val department: String,
  val crewList: List<SeriesCrew>,
)
