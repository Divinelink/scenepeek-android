package com.andreolas.movierama.home.ui

import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.UIText

data class HomeViewState(
    val isLoading: Boolean = false,
    val moviesList: List<PopularMovie>,
    val selectedMovie: PopularMovie? = null,
    val error: UIText? = null,
)
