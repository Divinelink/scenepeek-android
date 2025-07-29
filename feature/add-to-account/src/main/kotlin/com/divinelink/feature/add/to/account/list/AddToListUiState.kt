package com.divinelink.feature.add.to.account.list

import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.AddToListRoute
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AddToListUiState(
  val id: Int,
  val mediaType: MediaType,
  val page: Int,
  val lists: ListData<ListItem>,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val error: BlankSlateState?,
  val displayMessage: DisplayMessage?,
) {
  companion object {
    fun initial(addToListRoute: AddToListRoute) = AddToListUiState(
      id = addToListRoute.id,
      mediaType = addToListRoute.mediaType,
      page = 1,
      lists = ListData.Initial,
      isLoading = true,
      loadingMore = false,
      error = null,
      displayMessage = null,
    )
  }
}
