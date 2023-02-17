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
    val filters: List<Filter> = HomeFilters.values().map { it.filter },
    val moviesList: List<PopularMovie>,
    val searchMovies: List<PopularMovie>? = null,
    val selectedMovie: PopularMovie? = null,
    val loadMorePopular: Boolean = true,
    val query: String = "",
    val searchLoading: Boolean = false,
    val emptyResult: Boolean = false,
    val error: UIText? = null,
) {
    val initialLoading = isLoading && moviesList.isEmpty()
    val loadMore = isLoading && moviesList.isNotEmpty()
}

enum class HomeFilters(val filter: Filter) {
    POPULAR(Filter("Popular", false)),
    TOP_RATED(Filter("Top Rated", false)),
    UPCOMING(Filter("Upcoming", false)),
    NOW_PLAYING(Filter("Now Playing", false)),
}
