package com.divinelink.core.model.list.details

import com.divinelink.core.model.list.ListDetails

sealed interface ListDetailsData<out T> {
  data object Initial : ListDetailsData<Nothing>

  data class Data(val data: ListDetails) : ListDetailsData<ListDetails> {
    val isEmpty: Boolean = data.isEmpty()
  }
}
