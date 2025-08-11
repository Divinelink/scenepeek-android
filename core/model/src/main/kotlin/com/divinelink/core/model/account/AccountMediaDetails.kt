package com.divinelink.core.model.account

data class AccountMediaDetails(
  val id: Int,
  val favorite: Boolean,
  val rating: Float?,
  val watchlist: Boolean,
) {
  val beautifiedRating: Int? = rating?.toInt()

  companion object {
    val initial = AccountMediaDetails(
      id = -1,
      favorite = false,
      rating = null,
      watchlist = false,
    )
  }
}
