package com.divinelink.core.model.sort

enum class SortDirection(
  val value: String,
) {
  ASC("asc"),
  DESC("desc"),
  ;

  companion object {
    fun from(value: String?) = entries.find { it.value == value } ?: DESC
  }
}

fun SortDirection.other(): SortDirection = when (this) {
  SortDirection.ASC -> SortDirection.DESC
  SortDirection.DESC -> SortDirection.ASC
}
