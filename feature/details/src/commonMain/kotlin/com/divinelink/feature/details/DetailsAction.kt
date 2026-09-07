package com.divinelink.feature.details

sealed interface DetailsAction {
  data class MarkSeasonComplete(
    val seasonNumber: Int,
    val isComplete: Boolean,
  ) : DetailsAction
}
