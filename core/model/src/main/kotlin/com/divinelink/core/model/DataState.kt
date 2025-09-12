package com.divinelink.core.model

sealed interface DataState<out T> {
  data object Initial : DataState<Nothing>

  data class Data<T>(val data: PaginationData<T>) : DataState<T> {
    val isEmpty: Boolean = data.isEmpty()
  }
}
