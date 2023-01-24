package com.andreolas.movierama.home.ui

import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.UIText

/**
 * @param loadMorePopular indicates whether to load more popular movies when reaching the end of screen,
 * false otherwise.
 * @param moviesList a collection of movies list that are to be shown on the screen.
 *
 * */
data class HomeViewState(
    val isLoading: Boolean = false,
    val moviesList: List<PopularMovie>,
    val selectedMovie: PopularMovie? = null,
    val loadMorePopular: Boolean = true,
    val query: String = "",
    val error: UIText? = null,
)
