package com.divinelink.core.model.list.details

import com.divinelink.core.model.list.ListDetails

sealed class ListDetailsData<out T>(
  open val name: String,
  open val backdropPath: String?,
) {
  data class Initial(
    override val name: String,
    override val backdropPath: String?,
  ) : ListDetailsData<Nothing>(
    name = name,
    backdropPath = backdropPath,
  )

  data class Data(val data: ListDetails) :
    ListDetailsData<ListDetails>(
      name = data.name,
      backdropPath = data.backdropPath,
    ) {
    val isEmpty: Boolean = data.isEmpty()
    val canLoadMore: Boolean = data.canLoadMore()
  }
}
