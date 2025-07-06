package com.divinelink.feature.add.to.account.list

sealed interface AddToListAction {
  data object LoadMore : AddToListAction
  data object ConsumeDisplayMessage : AddToListAction
  data class OnListClick(val id: Int) : AddToListAction
}
