package com.divinelink.feature.search.ui

import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.ui.blankslate.BlankSlateState

data class SearchUiState(
  val query: String,
  val page: Int,
  val searchResults: MediaSection?,
  val error: BlankSlateState?,
  val isLoading: Boolean,
  val focusSearch: Boolean,
) {
  companion object {
    fun initial() = SearchUiState(
      page = 1,
      query = "",
      error = null,
      isLoading = false,
      searchResults = null,
      focusSearch = false,
    )
  }
}
