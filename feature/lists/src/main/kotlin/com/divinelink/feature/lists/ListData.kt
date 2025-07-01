package com.divinelink.feature.lists

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem

sealed interface ListData<out T> {
  data object Initial : ListData<Nothing>

  data class Data(val data: PaginationData<ListItem>) : ListData<ListItem> {
    val isEmpty: Boolean = data.isEmpty()
  }
}
