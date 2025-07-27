package com.divinelink.feature.lists.create.backdrop

data class SelectBackdropUiState(val backdrops: Map<String, String>) {
  companion object {
    val initial = SelectBackdropUiState(
      backdrops = emptyMap(),
    )
  }
}
