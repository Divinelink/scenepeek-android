package com.divinelink.scenepeek.home.ui

import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.Filter
import com.divinelink.scenepeek.R
import com.divinelink.core.ui.R as uiR

data class HomeViewState(
  val isLoading: Boolean,
  val filters: List<Filter>,
  val popularMovies: MediaSection,
  val searchResults: MediaSection?,
  val filteredResults: MediaSection?,
  val query: String,
  val isSearchLoading: Boolean,
  val error: BlankSlateState?,
  val mode: HomeMode,
  val pages: Map<HomePage, Int>,
  val retryAction: HomeMode?,
) {
  val initialLoading = isLoading && popularMovies.data.isEmpty()

  val showFavorites = filters.find { it.name == HomeFilter.Liked.filter.name }?.isSelected

  val isEmpty: Boolean = when (mode) {
    HomeMode.Browser -> popularMovies.data.isEmpty()
    HomeMode.Search -> searchResults?.data?.isEmpty() ?: true
    HomeMode.Filtered -> filteredResults?.data?.isEmpty() ?: true
  }

  private val emptyContentUiState: BlankSlateState? = when (mode) {
    HomeMode.Search -> BlankSlateState.Custom(
      icon = uiR.drawable.core_ui_search,
      title = UIText.ResourceText(R.string.search__empty_result_title),
      description = UIText.ResourceText(R.string.search__empty_result_description),
    )
    HomeMode.Filtered -> BlankSlateState.Custom(
      icon = uiR.drawable.core_ui_ghost,
      title = UIText.ResourceText(R.string.home__empty_filtered_result_title),
      description = UIText.ResourceText(R.string.home__empty_filtered_result_description),
    )
    else -> null
  }

  val blankSlate = if (mode == HomeMode.Filtered && isEmpty) {
    emptyContentUiState
  } else if (isEmpty && error == null) {
    emptyContentUiState
  } else {
    error
  }

  companion object {
    fun initial() = HomeViewState(
      isLoading = true,
      popularMovies = MediaSection(
        data = emptyList(),
        shouldLoadMore = true,
      ),
      filters = HomeFilter.entries.map { it.filter },
      searchResults = null,
      filteredResults = null,
      isSearchLoading = false,
      error = null,
      query = "",
      mode = HomeMode.Browser,
      pages = mapOf(
        HomePage.Popular to 1,
        HomePage.Search to 1,
      ),
      retryAction = null,
    )
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
