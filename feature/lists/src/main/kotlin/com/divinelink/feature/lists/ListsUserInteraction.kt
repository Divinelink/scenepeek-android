package com.divinelink.feature.lists

sealed interface ListsUserInteraction {
  data object LoadMore : ListsUserInteraction
}
