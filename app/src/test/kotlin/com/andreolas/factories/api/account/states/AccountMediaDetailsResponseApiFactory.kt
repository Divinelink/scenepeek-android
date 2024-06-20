package com.andreolas.factories.api.account.states

import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi

object AccountMediaDetailsResponseApiFactory {

  fun Rated(): AccountMediaDetailsResponseApi = AccountMediaDetailsResponseApi(
    id = 1234,
    favorite = false,
    rated = RateResponseApiFactory.rated(),
    watchlist = false,
  )

  fun NotRated(): AccountMediaDetailsResponseApi = AccountMediaDetailsResponseApi(
    id = 1234,
    favorite = false,
    rated = RateResponseApiFactory.`false`(),
    watchlist = false,
  )
}
