package com.andreolas.movierama.home.ui

import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.Filter
import com.andreolas.movierama.ui.components.bottomsheet.BottomSheetUiState

/**
 * @param loadMorePopular indicates whether to load more popularMovies movies when reaching the end of screen,
 * false otherwise.
 * @param popularMovies a collection of movies list that are to be shown on the screen.
 *
 * */
data class HomeViewState(
  val isLoading: Boolean = true,
  val filters: List<Filter> = HomeFilter.values().map { it.filter },
  val popularMovies: List<PopularMovie>,
  val searchResults: List<MediaItem>? = null,
  val filteredResults: List<MediaItem.Media>? = null,
  val selectedMovie: MediaItem.Media? = null,
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
    popularMovies.map {
      MediaItem.Media.Movie(
        id = it.id,
        name = it.title,
        posterPath = it.posterPath,
        releaseDate = it.releaseDate,
        rating = it.rating,
        overview = it.overview,
        isFavorite = it.isFavorite,
      )
    }
  }

  val bottomSheetUiState = if (selectedMovie != null) {
    BottomSheetUiState.Visible(selectedMovie)
  } else {
    BottomSheetUiState.Hidden
  }
}

enum class HomeFilter(val filter: Filter) {
  Liked(Filter(name = "Liked By You", isSelected = false)),
}
