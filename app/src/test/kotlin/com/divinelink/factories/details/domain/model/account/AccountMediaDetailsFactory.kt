package com.divinelink.factories.details.domain.model.account

import com.divinelink.core.model.account.AccountMediaDetails

object AccountMediaDetailsFactory {

  fun Rated(): AccountMediaDetails = AccountMediaDetails(
    id = 1234,
    favorite = false,
    rating = 8.0f,
    watchlist = false,
  )

  fun NotRated(): AccountMediaDetails = AccountMediaDetails(
    id = 1234,
    favorite = false,
    rating = null,
    watchlist = false,
  )

  fun initial(): AccountMediaDetails = AccountMediaDetails(
    id = -1,
    favorite = false,
    rating = null,
    watchlist = false,
  )

  class AccountMediaDetailsFactoryWizard(private var accountMediaDetails: AccountMediaDetails) {

    fun withId(id: Int) = apply {
      accountMediaDetails = accountMediaDetails.copy(id = id)
    }

    fun withRating(rating: Float?) = apply {
      accountMediaDetails = accountMediaDetails.copy(rating = rating)
    }

    fun withWatchlist(watchlist: Boolean) = apply {
      accountMediaDetails = accountMediaDetails.copy(watchlist = watchlist)
    }

    fun build(): AccountMediaDetails = accountMediaDetails
  }

  fun AccountMediaDetails.toWizard(block: AccountMediaDetailsFactoryWizard.() -> Unit) =
    AccountMediaDetailsFactoryWizard(this).apply(block).build()
}
