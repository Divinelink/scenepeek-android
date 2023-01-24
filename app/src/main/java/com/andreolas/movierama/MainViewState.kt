package com.andreolas.movierama

import com.andreolas.movierama.ui.UIText

sealed class MainViewState(
    open val isLoading: Boolean,
    open val error: UIText? = null,
) {

    object Loading : MainViewState(
        isLoading = true,
    )

    data class Error(
        override val error: UIText? = null,
    ) : MainViewState(
        isLoading = false,
        error = error,
    )

    object Completed : MainViewState(
        isLoading = false,
    )
}
