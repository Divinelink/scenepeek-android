package com.divinelink.feature.lists.create

data class CreateListUiState(
  val todo: Int,
  val todo2: Int,
) {
  companion object {
    val initial = CreateListUiState(
      todo = 0,
      todo2 = 0,
    )
  }
}
