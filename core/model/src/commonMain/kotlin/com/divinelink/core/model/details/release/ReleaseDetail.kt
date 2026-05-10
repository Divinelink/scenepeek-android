package com.divinelink.core.model.details.release

import kotlinx.datetime.LocalDateTime

data class ReleaseDetail(
  val note: String,
  val releaseDate: LocalDateTime,
  val type: ReleaseType,
)
