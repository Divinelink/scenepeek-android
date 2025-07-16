package com.divinelink.core.model.list.details

import com.divinelink.core.model.list.ListDetails

sealed class ListDetailsData<out T>(
  open val name: String,
  open val backdropPath: String?,
  open val description: String,
  open val public: Boolean,
) {
  data class Initial(
    override val name: String,
    override val backdropPath: String?,
    override val description: String,
    override val public: Boolean,
  ) : ListDetailsData<Nothing>(
    name = name,
    backdropPath = backdropPath,
    description = description,
    public = public,
  )

  data class Data(val data: ListDetails) :
    ListDetailsData<ListDetails>(
      name = data.name,
      backdropPath = data.backdropPath,
      description = data.description,
      public = data.public,
    ) {
    val isEmpty: Boolean = data.isEmpty()
    val canLoadMore: Boolean = data.canLoadMore()
  }
}
