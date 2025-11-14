package com.divinelink.core.model

sealed interface ItemState<out T> {
  data object Loading : ItemState<Nothing>
  data class Data<T>(
    val item: T,
    val loading: Boolean,
  ) : ItemState<T>
}

fun <T> ItemState<T>?.setLoading(loading: Boolean) = when (this) {
  is ItemState.Data -> this.copy(loading = loading)
  ItemState.Loading -> this
  null -> null
}
