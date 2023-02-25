package com.andreolas.movierama.home.ui

import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.Filter

/**
 * @param loadMorePopular indicates whether to load more popular movies when reaching the end of screen,
 * false otherwise.
 * @param moviesList a collection of movies list that are to be shown on the screen.
 *
 * */
data class HomeViewState(
    val isLoading: Boolean = true,
    val filters: List<Filter> = HomeFilter.values().map { it.filter },
    val moviesList: List<PopularMovie>,
    val searchMovies: List<PopularMovie>? = null,
    val filteredMovies: List<PopularMovie>? = null,
    val selectedMovie: PopularMovie? = null,
    val loadMorePopular: Boolean = true,
    val query: String = "",
    val searchLoading: Boolean = false,
    val emptyResult: Boolean = false,
    val error: UIText? = null,
) {
    val initialLoading = isLoading && moviesList.isEmpty()
    val loadMore = isLoading && moviesList.isNotEmpty()

    val hasFiltersSelected = filters.any { it.isSelected }

    val showFavorites = filters.find { it.name == HomeFilter.Liked.filter.name }?.isSelected
}

fun HomeViewState.getMoviesList(): List<PopularMovie> {
    return if (searchMovies?.isNotEmpty() == true) {
        searchMovies
    } else if (filteredMovies?.isNotEmpty() == true) {
        filteredMovies
    } else {
        moviesList
    }
}

enum class HomeFilter(val filter: Filter) {
    Liked(Filter(name = "Liked By You", isSelected = false)),
}
