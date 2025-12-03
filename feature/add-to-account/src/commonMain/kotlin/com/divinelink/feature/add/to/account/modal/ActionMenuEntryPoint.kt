package com.divinelink.feature.add.to.account.modal

sealed interface ActionMenuEntryPoint {
  data class ListDetails(
    val listId: Int,
    val listName: String,
  ) : ActionMenuEntryPoint

  data object Other : ActionMenuEntryPoint
}
