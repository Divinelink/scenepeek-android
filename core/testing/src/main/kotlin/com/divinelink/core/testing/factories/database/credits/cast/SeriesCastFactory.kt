package com.divinelink.core.testing.factories.database.credits.cast

import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object SeriesCastFactory {

  fun brianBaumgartner() = SeriesCast(
    id = 94622,
    name = "Brian Baumgartner",
    originalName = "Brian Baumgartner",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    totalEpisodeCount = 217,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun angelaKinsey() = SeriesCast(
    id = 113867,
    name = "Angela Kinsey",
    originalName = "Angela Kinsey",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
    totalEpisodeCount = 210,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun allCast() = listOf(
    brianBaumgartner(),
    angelaKinsey(),
  )
}
