package com.divinelink.feature.lists.details

data class ListDetailsUiState(
  val id: Int,
  val name: String,
) {
  companion object {
    fun initial(
      id: Int,
      name: String,
    ) = ListDetailsUiState(
      id = id,
      name = name,
    )
  }
}
