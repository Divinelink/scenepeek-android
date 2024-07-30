package com.andreolas.movierama

import com.divinelink.core.navigation.arguments.DetailsNavArguments

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class NavigateToDetails(val navArgs: DetailsNavArguments) : MainUiEvent
}
