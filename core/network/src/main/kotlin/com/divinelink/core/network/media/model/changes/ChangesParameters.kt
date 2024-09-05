package com.divinelink.core.network.media.model.changes

data class ChangesParameters(
  val page: Int = 1,
  val startDate: String,
  val endDate: String,
)
