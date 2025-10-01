package com.divinelink.feature.discover

data class DiscoverUiState(
  val todo: Int,
  val todo2: Int,
) {
  companion object {
    val initial = DiscoverUiState(
      todo = 0,
      todo2 = 0,
    )
  }
}
