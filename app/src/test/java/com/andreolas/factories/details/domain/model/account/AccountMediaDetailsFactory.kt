package com.andreolas.factories.details.domain.model.account

import com.divinelink.core.model.account.AccountMediaDetails

object AccountMediaDetailsFactory {

  fun Rated(): AccountMediaDetails =
    AccountMediaDetails(
      id = 1,
      favorite = false,
      rating = 8.0f,
      watchlist = false
    )

  fun NotRated(): AccountMediaDetails =
    AccountMediaDetails(
      id = 2,
      favorite = false,
      rating = null,
      watchlist = false
    )
}
