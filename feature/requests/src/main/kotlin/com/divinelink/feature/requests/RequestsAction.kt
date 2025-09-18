package com.divinelink.feature.requests

import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaType

sealed interface RequestsAction {
  data object LoadMore : RequestsAction
  data class FetchMediaItem(val request: RequestUiItem) : RequestsAction

  data class ApproveRequest(val id: Int) : RequestsAction
  data class CancelRequest(val id: Int) : RequestsAction
  data class DeclineRequest(val id: Int) : RequestsAction
  data class DeleteRequest(val id: Int) : RequestsAction
  data class RemoveFromServer(
    val id: Int,
    val mediaType: MediaType,
  ) : RequestsAction

  data class EditRequest(val id: Int) : RequestsAction
  data class RetryRequest(val id: Int) : RequestsAction
}
