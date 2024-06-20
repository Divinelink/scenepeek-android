package com.andreolas.factories.api.account.states

import com.divinelink.core.network.media.model.states.RateResponseApi

object RateResponseApiFactory {

  fun rated(): RateResponseApi = RateResponseApi.Value(
    value = 8.0f,
  )

  fun `false`(): RateResponseApi = RateResponseApi.False
}
