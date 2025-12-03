package com.divinelink.feature.lists.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class ListDetailsUiState(
  val id: Int,
  val page: Int,
  val details: ListDetailsData<ListDetails>,
  val error: BlankSlateState?,
  val refreshing: Boolean,
  val loadingMore: Boolean,
  val selectedMedia: List<MediaItem.Media>,
  val multipleSelectMode: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    fun initial(
      id: Int,
      name: String,
      backdropPath: String?,
      description: String,
      public: Boolean,
    ) = ListDetailsUiState(
      id = id,
      page = 1,
      details = ListDetailsData.Initial(
        name = name,
        backdropPath = backdropPath,
        description = description,
        public = public,
      ),
      error = null,
      refreshing = false,
      loadingMore = false,
      selectedMedia = emptyList(),
      multipleSelectMode = false,
      snackbarMessage = null,
    )
  }

  fun canLoadMore(): Boolean = !loadingMore &&
    details is ListDetailsData.Data &&
    details.canLoadMore
}
