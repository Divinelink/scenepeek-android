package com.divinelink.feature.media.lists

sealed interface MediaListsAction {
  data object Retry : MediaListsAction
  data object LoadMore : MediaListsAction

  data class OnSelectTab(val index: Int) : MediaListsAction
}
