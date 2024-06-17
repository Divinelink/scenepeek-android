package com.divinelink.core.network

data class PaginationData<T>(
  val totalPages: Int,
  val list: List<T>
)
