package com.divinelink.feature.discover

sealed interface DiscoverAction {
  data class OnSelectTab(val index: Int) : DiscoverAction
}
