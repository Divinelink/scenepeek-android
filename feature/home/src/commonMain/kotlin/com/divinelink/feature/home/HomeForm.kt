package com.divinelink.feature.home

import com.divinelink.core.model.media.MediaItem

sealed interface HomeForm<out T : MediaItem> {
  data object Initial : HomeForm<Nothing>
  data object Error : HomeForm<Nothing>

  data class Data<T : MediaItem>(
    val pages: Map<Int, List<T>>,
    val canLoadMore: Boolean,
    val isLoading: Boolean,
    val hasError: Boolean,
  ) : HomeForm<T> {
    val media = pages.values.flatten().distinctBy { it.uniqueIdentifier }
  }
}
