package com.divinelink.core.ui

import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class NavigateToDetails(val route: DetailsRoute) : MainUiEvent
  data class NavigateToPersonDetails(val route: PersonRoute) : MainUiEvent
}
