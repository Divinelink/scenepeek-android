package com.divinelink.core.model.account

data class AccountMediaDetails(
  val id: Int,
  val favorite: Boolean,
  val rating: Float?,
  val watchlist: Boolean,
) {
  val beautifiedRating: String? = rating?.toInt()?.toString()
}
