package com.divinelink.core.database.credits.model

import com.divinelink.core.database.credits.cast.SeriesCastRole

data class CastEntity(
  val id: Long,
  val name: String,
  val originalName: String,
  val profilePath: String?,
  val knownForDepartment: String?,
  val gender: Long,
  val roles: List<SeriesCastRole>,
)
