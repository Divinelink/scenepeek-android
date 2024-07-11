package com.andreolas.movierama

import com.divinelink.feature.details.ui.DetailsNavArguments

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class NavigateToDetails(val navArgs: DetailsNavArguments) : MainUiEvent
}
