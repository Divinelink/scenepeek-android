package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class SavedExport(
  val movies: Set<Long>,
  val tv: Set<Long>,
  val people: Set<Long>,
)
