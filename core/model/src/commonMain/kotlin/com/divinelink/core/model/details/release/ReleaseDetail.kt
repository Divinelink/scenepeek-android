package com.divinelink.core.model.details.release

import kotlinx.datetime.LocalDate

data class ReleaseDetail(
  val note: String,
  val releaseDate: LocalDate,
  val type: ReleaseType,
)
