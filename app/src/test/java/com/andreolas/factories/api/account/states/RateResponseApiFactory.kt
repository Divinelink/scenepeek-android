package com.andreolas.factories.api.account.states

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.RateResponseApi

object RateResponseApiFactory {

  fun Rated(): RateResponseApi = RateResponseApi.Value(
    value = 8.0f
  )

  fun False(): RateResponseApi = RateResponseApi.False
}
