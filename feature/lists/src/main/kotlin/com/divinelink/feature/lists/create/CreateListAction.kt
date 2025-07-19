package com.divinelink.feature.lists.create

sealed interface CreateListAction {
  data class NameChanged(val name: String) : CreateListAction
  data class DescriptionChanged(val description: String) : CreateListAction

  data object PublicChanged : CreateListAction

  data object CreateOrEditList : CreateListAction
  data object DeleteList : CreateListAction

  data object DismissSnackbar : CreateListAction
}
