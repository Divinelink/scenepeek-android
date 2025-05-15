package com.divinelink.core.model.details.review

data class Review(
  val author: Author,
  val rating: Int?,
  val content: String,
  val date: String?,
)
