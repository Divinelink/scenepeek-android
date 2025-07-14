package com.divinelink.feature.lists.user

sealed interface ListsAction {
  data object LoadMore : ListsAction
  data class OnListClick(
    val id: Int,
    val name: String,
  ) : ListsAction
}
