package com.andreolas.factories.api.account.states

import com.divinelink.core.network.movies.model.states.AccountMediaDetailsResponseApi

object AccountMediaDetailsResponseApiFactory {

  fun Rated(): AccountMediaDetailsResponseApi =
    AccountMediaDetailsResponseApi(
      id = 1,
      favorite = false,
      rated = RateResponseApiFactory.Rated(),
      watchlist = false
    )

  fun NotRated(): AccountMediaDetailsResponseApi =
    AccountMediaDetailsResponseApi(
      id = 2,
      favorite = false,
      rated = RateResponseApiFactory.False(),
      watchlist = false
    )
}
