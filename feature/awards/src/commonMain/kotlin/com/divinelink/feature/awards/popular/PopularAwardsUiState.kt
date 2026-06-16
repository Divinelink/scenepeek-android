package com.divinelink.feature.awards.categories

data class AwardCategoriesUiState(
  val todo: Int,
  val todo2: Int,
) {
  companion object {
    val initial = AwardCategoriesUiState(
      todo = 0,
      todo2 = 0,
    )
  }
}
