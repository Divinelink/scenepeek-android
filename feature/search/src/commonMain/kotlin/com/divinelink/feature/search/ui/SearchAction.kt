package com.divinelink.feature.search.ui

import com.divinelink.core.model.media.MediaReference

sealed interface SearchAction {
  data class AddToHistory(val id: Long, val type: String) : SearchAction
  data class RemoveFromHistory(val media: MediaReference) : SearchAction
  data object ClearHistory : SearchAction
}
