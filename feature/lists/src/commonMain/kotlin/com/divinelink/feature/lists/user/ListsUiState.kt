package com.divinelink.feature.lists.user

import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.blankslate.BlankSlateState

data class ListsUiState(
  val page: Int,
  val lists: ListData<ListItem>,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val refreshing: Boolean,
  val error: BlankSlateState?,
) {
  companion object {
    val initial = ListsUiState(
      page = 1,
      lists = ListData.Initial,
      isLoading = true,
      loadingMore = false,
      refreshing = false,
      error = null,
    )
  }
}
