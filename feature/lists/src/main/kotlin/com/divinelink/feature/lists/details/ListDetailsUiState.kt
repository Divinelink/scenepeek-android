package com.divinelink.feature.lists.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.ui.blankslate.BlankSlateState

data class ListDetailsUiState(
  val id: Int,
  val name: String,
  val page: Int,
  val details: ListDetailsData<ListDetails>,
  val error: BlankSlateState?,
  val refreshing: Boolean,
  val loadingMore: Boolean,
) {
  companion object {
    fun initial(
      id: Int,
      name: String,
    ) = ListDetailsUiState(
      id = id,
      name = name,
      page = 1,
      details = ListDetailsData.Initial,
      error = null,
      refreshing = false,
      loadingMore = false,
    )
  }

  fun canLoadMore(): Boolean = !loadingMore &&
    details is ListDetailsData.Data &&
    details.canLoadMore
}
