package com.divinelink.feature.lists

data class ListsUiState(val isLoading: Boolean) {
  companion object {
    val initial = ListsUiState(isLoading = true)
  }
}
