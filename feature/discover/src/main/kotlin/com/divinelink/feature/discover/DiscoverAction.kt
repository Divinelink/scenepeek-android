package com.divinelink.feature.discover

sealed interface DiscoverAction {
  data object ClearFilters : DiscoverAction
  data object DiscoverMedia : DiscoverAction
  data object LoadMore : DiscoverAction
  data class OnSelectTab(val index: Int) : DiscoverAction
}
