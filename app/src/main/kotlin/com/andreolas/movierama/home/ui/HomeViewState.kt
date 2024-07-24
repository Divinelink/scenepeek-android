package com.andreolas.movierama.home.ui

import com.andreolas.movierama.R
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.ui.EmptyContentUiState
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.Filter

data class HomeViewState(
  val isLoading: Boolean,
  val filters: List<Filter>,
  val popularMovies: MediaSection,
  val searchResults: MediaSection?,
  val filteredResults: MediaSection?,
  val query: String,
  val isSearchLoading: Boolean,
  val error: UIText?,
  val mode: HomeMode,
) {
  val initialLoading = isLoading && popularMovies.data.isEmpty()

  val showFavorites = filters.find { it.name == HomeFilter.Liked.filter.name }?.isSelected

  val isEmpty: Boolean = when (mode) {
    HomeMode.Browser -> popularMovies.data.isEmpty()
    HomeMode.Search -> searchResults?.data?.isEmpty() ?: true
    HomeMode.Filtered -> filteredResults?.data?.isEmpty() ?: true
  }

  val emptyContentUiState: EmptyContentUiState? = when (mode) {
    HomeMode.Search -> EmptyContentUiState(
      icon = com.divinelink.core.ui.R.drawable.core_ui_ic_error_64,
      title = UIText.ResourceText(R.string.search__empty_result_title),
      description = UIText.ResourceText(R.string.search__empty_result_description),
    )
    HomeMode.Filtered -> EmptyContentUiState(
      icon = com.divinelink.core.ui.R.drawable.core_ui_ic_error_64,
      title = UIText.ResourceText(R.string.home__empty_filtered_result_title),
      description = UIText.ResourceText(R.string.home__empty_filtered_result_description),
    )
    else -> null
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
    )
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
