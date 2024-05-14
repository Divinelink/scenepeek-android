package com.andreolas.movierama

import com.andreolas.movierama.ui.UIText

sealed class MainViewState(
  open val isLoading: Boolean,
  open val error: UIText? = null,
) {

  data object Loading : MainViewState(
    isLoading = true,
  )

  data object Completed : MainViewState(
    isLoading = false,
  )
}
