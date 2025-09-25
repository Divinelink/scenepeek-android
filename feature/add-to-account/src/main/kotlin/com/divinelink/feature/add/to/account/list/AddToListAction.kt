package com.divinelink.feature.add.to.account.list

sealed interface AddToListAction {
  data object Login : AddToListAction
  data object LoadMore : AddToListAction
  data class CheckItemStatus(val id: Int) : AddToListAction
  data class OnListClick(val id: Int) : AddToListAction

  data object ConsumeDisplayMessage : AddToListAction
}
