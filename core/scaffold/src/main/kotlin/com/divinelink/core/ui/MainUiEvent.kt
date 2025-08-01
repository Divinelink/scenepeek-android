package com.divinelink.core.ui

import com.divinelink.core.navigation.route.Navigation

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class NavigateToDetails(val route: Navigation.DetailsRoute) : MainUiEvent
  data class NavigateToPersonDetails(val route: Navigation.PersonRoute) : MainUiEvent
}
