package com.divinelink.core.model.details

data class SeasonProgress(
  val seasonNumber: Int,
  val watchedCount: Int,
) {
  companion object {
    val empty = SeasonProgress(
      seasonNumber = -1,
      watchedCount = -1,
    )
  }
}
