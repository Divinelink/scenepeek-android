package com.divinelink.feature.add.to.account.list

import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AddToListUiState(
  val page: Int,
  val lists: ListData<ListItem>,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val error: BlankSlateState?,
) {
  companion object {
    val initial = AddToListUiState(
      page = 1,
      lists = ListData.Initial,
      isLoading = true,
      loadingMore = false,
      error = null,
    )
  }
}
