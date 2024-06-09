package com.andreolas.movierama.home.ui

import com.andreolas.movierama.ui.components.Filter
import com.divinelink.core.model.media.MediaItem
import com.divinelink.ui.UIText

/**
 * @param loadMorePopular indicates whether to load more popularMovies movies when reaching the end of screen,
 * false otherwise.
 * @param popularMovies a collection of movies list that are to be shown on the screen.
 * */
data class HomeViewState(
  val isLoading: Boolean = true,
  val filters: List<Filter> = HomeFilter.entries.map { it.filter },
  val popularMovies: List<MediaItem.Media.Movie>,
  val searchResults: List<MediaItem>? = null,
  val filteredResults: List<MediaItem.Media>? = null,
  val loadMorePopular: Boolean = true,
  val query: String = "",
  val searchLoadingIndicator: Boolean = false,
  val emptyResult: Boolean = false,
  val error: UIText? = null,
) {
  val initialLoading = isLoading && popularMovies.isEmpty()
  val loadMore = isLoading && popularMovies.isNotEmpty()

  val hasFiltersSelected = filters.any { it.isSelected }

  val showFavorites = filters.find { it.name == HomeFilter.Liked.filter.name }?.isSelected

  val searchList = if (searchResults?.isNotEmpty() == true) {
    searchResults
  } else {
    popularMovies
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
