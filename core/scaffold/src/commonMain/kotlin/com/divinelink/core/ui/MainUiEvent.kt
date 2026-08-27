package com.divinelink.core.ui

import com.divinelink.core.navigation.route.Navigation

sealed interface MainUiEvent {
  data object None : MainUiEvent
  data class Navigate(val route: Navigation) : MainUiEvent
  data class OpenInBrowser(val uri: String) : MainUiEvent
}
