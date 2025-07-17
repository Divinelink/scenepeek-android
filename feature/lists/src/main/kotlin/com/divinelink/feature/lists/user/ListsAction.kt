package com.divinelink.feature.lists.user

import com.divinelink.core.model.ui.ViewMode

sealed interface ListsAction {
  data object LoadMore : ListsAction
  data class OnListClick(
    val id: Int,
    val name: String,
    val backdropPath: String?,
    val description: String,
    val public: Boolean,
  ) : ListsAction

  data class UpdateViewMode(val viewMode: ViewMode) : ListsAction
}
