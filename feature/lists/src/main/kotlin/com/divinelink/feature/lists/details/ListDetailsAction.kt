package com.divinelink.feature.lists.details

import com.divinelink.core.model.media.MediaType

sealed interface ListDetailsAction {
  data object LoadMore : ListDetailsAction
  data object Refresh : ListDetailsAction

  data class OnItemClick(
    val mediaId: Int,
    val mediaType: MediaType,
  ) : ListDetailsAction
}
