package com.divinelink.feature.lists.details

import com.divinelink.core.model.media.MediaType

sealed interface ListDetailsAction {
  data object LoadMore : ListDetailsAction
  data object Refresh : ListDetailsAction

  data class OnItemClick(
    val mediaId: Int,
    val mediaType: MediaType,
  ) : ListDetailsAction

  data class SelectMedia(val mediaId: Int) : ListDetailsAction
  data object OnDeselectAll : ListDetailsAction
  data object OnSelectAll : ListDetailsAction
  data object OnDismissMultipleSelect : ListDetailsAction
}
