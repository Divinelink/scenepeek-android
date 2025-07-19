package com.divinelink.feature.lists.user

sealed interface ListsAction {
  data object LoadMore : ListsAction
  data object Refresh : ListsAction
  data class OnListClick(
    val id: Int,
    val name: String,
    val backdropPath: String?,
    val description: String,
    val public: Boolean,
  ) : ListsAction

  data object SwitchViewMode : ListsAction
}
