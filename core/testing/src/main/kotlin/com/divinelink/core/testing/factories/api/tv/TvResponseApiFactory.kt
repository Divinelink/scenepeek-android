package com.divinelink.core.testing.factories.api.tv

import com.divinelink.core.network.media.model.tv.TvResponseApi

object TvResponseApiFactory {

  fun full() = TvResponseApi(
    page = 1,
    results = TvItemApiFactory.all(),
    totalPages = 3,
    totalResults = 60,
  )
}
