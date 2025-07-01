package com.divinelink.feature.lists

sealed interface ListsUserInteraction {
  data object LoadMore : ListsUserInteraction
  data class OnListClick(val id: Int) : ListsUserInteraction
}
