package com.divinelink.feature.requests

import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.RequestUiItem

sealed interface RequestsAction {
  data object LoadMore : RequestsAction
  data class FetchMediaItem(val request: RequestUiItem) : RequestsAction

  data class UpdateFilter(val filter: MediaRequestFilter) : RequestsAction

  data class ApproveRequest(val id: Int) : RequestsAction
  data class CancelRequest(val id: Int) : RequestsAction
  data class DeclineRequest(val id: Int) : RequestsAction
  data class DeleteRequest(val id: Int) : RequestsAction
  data class RemoveFromServer(
    val mediaId: Int,
    val requestId: Int,
  ) : RequestsAction

  data class EditRequest(val request: JellyseerrRequest) : RequestsAction
  data class RetryRequest(val id: Int) : RequestsAction

  data class UpdateRequestInfo(val request: JellyseerrRequest) : RequestsAction
  data class OnRequestCanceled(val requestId: Int) : RequestsAction
}
