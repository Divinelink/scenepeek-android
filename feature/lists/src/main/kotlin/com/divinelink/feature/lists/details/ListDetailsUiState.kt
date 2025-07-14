package com.divinelink.feature.lists.details

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.details.ListDetailsData

data class ListDetailsUiState(
  val id: Int,
  val name: String,
  val page: Int,
  val details: ListDetailsData<ListDetails>,
) {
  companion object {
    fun initial(
      id: Int,
      name: String,
    ) = ListDetailsUiState(
      id = id,
      name = name,
      page = 1,
      details = ListDetailsData.Initial,
    )
  }
}
