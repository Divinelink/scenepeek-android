package com.andreolas.factories.api.account.states

import com.divinelink.core.network.media.model.states.RateResponseApi

object RateResponseApiFactory {

  fun Rated(): RateResponseApi = RateResponseApi.Value(
    value = 8.0f
  )

  fun False(): RateResponseApi = RateResponseApi.False
}
