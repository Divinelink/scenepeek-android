package com.andreolas.movierama.base.data.remote.movies.mapper

import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.model.states.RateResponseApi

fun AccountMediaDetailsResponseApi.map() = AccountMediaDetails(
  id = id,
  favorite = favorite,
  rating = when (rated) {
    is RateResponseApi.Value -> (rated as RateResponseApi.Value).value
    is RateResponseApi.False -> null
  },
  watchlist = watchlist,
)
