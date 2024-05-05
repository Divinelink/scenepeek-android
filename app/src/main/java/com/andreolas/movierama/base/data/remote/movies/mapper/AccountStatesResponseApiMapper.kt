package com.andreolas.movierama.base.data.remote.movies.mapper

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.account.states.RateResponseApi
import com.andreolas.movierama.details.domain.model.account.AccountMediaDetails

fun AccountMediaDetailsResponseApi.map() = AccountMediaDetails(
  id = id,
  favorite = favorite,
  rating = when (rated) {
    is RateResponseApi.Value -> rated.value
    is RateResponseApi.False -> null
  },
  watchlist = watchlist,
)
