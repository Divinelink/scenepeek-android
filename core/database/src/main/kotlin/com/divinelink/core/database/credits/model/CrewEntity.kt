package com.divinelink.core.database.credits.model

import com.divinelink.core.database.credits.crew.SeriesCrewJob

data class CrewEntity(
  val id: Long,
  val name: String,
  val originalName: String,
  val profilePath: String?,
  val department: String,
  val gender: Long,
  val knownForDepartment: String?,
  val roles: List<SeriesCrewJob>,
)
