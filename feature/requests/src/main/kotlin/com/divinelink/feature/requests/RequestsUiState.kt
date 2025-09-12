package com.divinelink.feature.requests

import com.divinelink.core.model.DataState
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestFilter
import com.divinelink.core.ui.blankslate.BlankSlateState

data class RequestsUiState(
  val page: Int,
  val filter: MediaRequestFilter,
  val data: DataState<JellyseerrRequest>,
  val isLoading: Boolean,
  val loadingMore: Boolean,
  val refreshing: Boolean,
  val error: BlankSlateState?,
) {
  companion object {
    val initial = RequestsUiState(
      page = 1,
      filter = MediaRequestFilter.Pending,
      data = DataState.Initial,
      isLoading = false,
      loadingMore = false,
      refreshing = false,
      error = null,
    )
  }
}
