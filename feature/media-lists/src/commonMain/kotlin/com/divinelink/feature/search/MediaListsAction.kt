package com.divinelink.feature.search

sealed interface MediaListsAction {
  data object Retry : MediaListsAction
  data object LoadMore : MediaListsAction
}
