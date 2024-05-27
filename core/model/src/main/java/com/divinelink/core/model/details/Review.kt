package com.divinelink.core.model.details

data class Review(
  val authorName: String,
  val rating: Int?,
  val content: String,
  val date: String?,
)
