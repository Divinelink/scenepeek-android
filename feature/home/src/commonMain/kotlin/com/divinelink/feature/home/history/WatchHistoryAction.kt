package com.divinelink.feature.home.history

sealed interface WatchHistoryAction {
  data object LoadMore : WatchHistoryAction
}
