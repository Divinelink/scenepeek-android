package com.divinelink.core.model.list

import com.divinelink.core.model.PaginationData

sealed interface ListData<out T> {
  data object Initial : ListData<Nothing>

  data class Data(val data: PaginationData<ListItem>) : ListData<ListItem> {
    val isEmpty: Boolean = data.isEmpty()
  }
}
