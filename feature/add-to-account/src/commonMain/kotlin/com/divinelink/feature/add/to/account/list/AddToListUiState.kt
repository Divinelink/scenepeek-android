package com.divinelink.feature.add.to.account.list

import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.list.ListData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation.AddToListRoute
import com.divinelink.core.ui.blankslate.BlankSlateState

data class AddToListUiState(
  val media: MediaReference,
  val page: Int,
  val lists: ListData<ListItem>,
  val addedToLists: Set<Int>,
  val itemsChecked: Set<Int>,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val error: BlankSlateState?,
  val displayMessage: DisplayMessage?,
) {
  companion object {
    fun initial(addToListRoute: AddToListRoute) = AddToListUiState(
      media = MediaReference(
        mediaId = addToListRoute.id,
        mediaType = MediaType.from(addToListRoute.mediaType),
      ),
      page = 1,
      lists = ListData.Initial,
      addedToLists = emptySet(),
      itemsChecked = emptySet(),
      isLoading = true,
      loadingMore = false,
      error = null,
      displayMessage = null,
    )
  }
}
