package com.andreolas.factories.api.account.states

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsResponseApi

object AccountMediaDetailsResponseApiFactory {

  fun Rated(): AccountMediaDetailsResponseApi = AccountMediaDetailsResponseApi(
    id = 1,
    favorite = false,
    rated = RateResponseApiFactory.Rated(),
    watchlist = false
  )

  fun NotRated(): AccountMediaDetailsResponseApi = AccountMediaDetailsResponseApi(
    id = 2,
    favorite = false,
    rated = RateResponseApiFactory.False(),
    watchlist = false
  )
}
