package com.divinelink.feature.awards.popular

data class AwardsUiState(
  val todo: Int,
  val todo2: Int,
) {
  companion object {
    val initial = AwardsUiState(
      todo = 0,
      todo2 = 0,
    )
  }
}
