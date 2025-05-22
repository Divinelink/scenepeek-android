package com.divinelink.core.model

data class PaginationData<T>(
  val totalPages: Int,
  val totalResults: Int,
  val list: List<T>,
)
