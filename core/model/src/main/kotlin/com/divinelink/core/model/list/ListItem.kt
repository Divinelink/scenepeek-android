package com.divinelink.core.model.list

data class ListItem(
  val name: String,
  val posterPath: String? = null,
  val backdropPath: String? = null,
  val description: String,
  val public: Boolean,
)
