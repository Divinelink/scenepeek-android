package com.divinelink.core.model

sealed class LCEState<in T> {
  data object Loading : LCEState<Any>()
  data class Content<T>(val data: T) : LCEState<T>()
  data object Error : LCEState<Any>()
}
