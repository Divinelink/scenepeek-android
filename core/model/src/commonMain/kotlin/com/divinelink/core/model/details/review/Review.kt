package com.divinelink.core.model.details.review

import kotlinx.datetime.LocalDate

data class Review(
  val author: Author,
  val rating: Int?,
  val content: String,
  val date: LocalDate?,
  val url: String,
)
