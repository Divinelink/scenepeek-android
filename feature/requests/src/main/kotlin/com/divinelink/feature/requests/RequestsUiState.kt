package com.divinelink.feature.requests

data class RequestsUiState(
  val todo: Int,
  val todo2: Int,
) {
  companion object {
    val initial = RequestsUiState(
      todo = 0,
      todo2 = 0,
    )
  }
}
