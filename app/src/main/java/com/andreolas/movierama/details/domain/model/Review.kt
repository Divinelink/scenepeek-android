package com.andreolas.movierama.details.domain.model

data class Review(
  val authorName: String,
  val rating: Int?,
  val content: String,
  val date: String?,
)
