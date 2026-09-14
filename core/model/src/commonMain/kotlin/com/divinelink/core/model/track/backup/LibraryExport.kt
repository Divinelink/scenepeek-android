package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class LibraryExport(
  val version: Int = 1,
  val exportedAt: String,
  val shows: List<ShowExport>,
)
