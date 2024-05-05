package com.andreolas.movierama.details.domain.model.account

data class AccountMediaDetails(
  val id: Int,
  val favorite: Boolean,
  val rating: Float?,
  val watchlist: Boolean,
)
