package com.andreolas.movierama.base.data.remote.movies.dto.account.states

import kotlinx.serialization.Serializable

@Serializable
data class AccountMediaDetailsResponseApi(
  val id: Int,
  val favorite: Boolean,
  val rated: RateResponseApi,
  val watchlist: Boolean,
)
