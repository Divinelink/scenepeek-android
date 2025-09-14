package com.divinelink.core.model

sealed interface ItemState<out T> {
  data object Loading : ItemState<Nothing>
  data class Data<T>(val item: T) : ItemState<T>
}
