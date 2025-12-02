@file:Suppress("MemberNameEqualsClassName")

package com.divinelink.core.model

sealed interface DataState<out T> {
  data object Initial : DataState<Nothing>

  data class Data<T>(val pages: Map<Int, List<T>>) : DataState<T> {
    val data = pages.values.flatten()
    val isEmpty: Boolean = data.isEmpty()
  }
}
