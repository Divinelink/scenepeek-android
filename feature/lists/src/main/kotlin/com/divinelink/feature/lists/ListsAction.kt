package com.divinelink.feature.lists

sealed interface ListsAction {
  data object LoadMore : ListsAction
  data class OnListClick(val id: Int) : ListsAction
}
