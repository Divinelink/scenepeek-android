package com.divinelink.feature.media.lists.ui

import com.divinelink.core.model.media.MediaItem

sealed interface MediaListsForm<out T : MediaItem> {
  data object Initial : MediaListsForm<Nothing>

  sealed interface Error : MediaListsForm<Nothing> {
    data object Offline : Error
    data object Generic : Error
  }

  data class Data<T : MediaItem>(
    val pages: Map<Int, List<T>>,
    val canLoadMore: Boolean,
    val isLoading: Boolean,
    val hasError: Boolean,
  ) : MediaListsForm<T> {
    val media = pages.values.flatten().distinctBy { it.uniqueIdentifier }
  }
}
