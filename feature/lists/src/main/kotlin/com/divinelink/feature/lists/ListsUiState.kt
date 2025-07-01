package com.divinelink.feature.lists

import com.divinelink.core.ui.blankslate.BlankSlateState

data class ListsUiState(
  val page: Int,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val error: BlankSlateState?,
) {
  companion object {
    val initial = ListsUiState(
      page = 1,
      isLoading = true,
      loadingMore = false,
      error = null,
    )
  }
}
