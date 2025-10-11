package com.divinelink.feature.discover

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.blankslate.BlankSlateState

sealed interface DiscoverForm<out T : MediaItem.Media> {
  data object Initial : DiscoverForm<Nothing>
  data object Loading : DiscoverForm<Nothing>

  sealed class Error(val blankSlate: BlankSlateState) : DiscoverForm<Nothing> {
    data object Network : Error(BlankSlateState.Offline)
    data object Unknown : Error(BlankSlateState.Generic)
  }

  data class Data<T : MediaItem.Media>(
    val mediaType: MediaType,
    val paginationData: Map<Int, List<MediaItem.Media>>,
    val totalResults: Int,
  ) : DiscoverForm<T> {
    val media = paginationData.values.flatten()
    val isEmpty: Boolean = media.isEmpty()
  }
}
