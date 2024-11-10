package com.divinelink.scenepeek

import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class NavigateToDetails(val navArgs: DetailsNavArguments) : MainUiEvent
  data class NavigateToPersonDetails(val navArgs: PersonNavArguments) : MainUiEvent
}
