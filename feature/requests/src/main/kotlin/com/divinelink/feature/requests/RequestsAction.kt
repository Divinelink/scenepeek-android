package com.divinelink.feature.requests

import com.divinelink.core.model.jellyseerr.media.RequestUiItem

sealed interface RequestsAction {
  data object LoadMore : RequestsAction
  data class FetchMediaItem(val request: RequestUiItem) : RequestsAction
}
