package com.divinelink.feature.add.to.account.list

sealed interface AddToListUserInteraction {
  data object LoadMore : AddToListUserInteraction
  data class OnListClick(val id: Int) : AddToListUserInteraction
}
