package com.divinelink.feature.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.divinelink.core.navigation.route.SearchRoute

class SearchViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

  private val route: SearchRoute = SearchRoute(
    focus = false,
  )
}
