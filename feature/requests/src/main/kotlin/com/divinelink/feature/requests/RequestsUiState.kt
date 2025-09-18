package com.divinelink.feature.requests

import com.divinelink.core.model.DataState
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import com.divinelink.core.model.jellyseerr.request.MediaRequestFilter
import com.divinelink.core.ui.blankslate.BlankSlateState

data class RequestsUiState(
  val page: Int,
  val filter: MediaRequestFilter,
  val data: DataState<RequestUiItem>,
  val loading: Boolean,
  val loadingMore: Boolean,
  val canLoadMore: Boolean,
  val refreshing: Boolean,
  val error: BlankSlateState?,
  val permissions: List<ProfilePermission>,
) {
  companion object {
    val initial = RequestsUiState(
      page = 1,
      filter = MediaRequestFilter.Pending,
      data = DataState.Initial,
      loading = false,
      loadingMore = false,
      canLoadMore = false,
      refreshing = false,
      error = null,
      permissions = emptyList(),
    )
  }
}
