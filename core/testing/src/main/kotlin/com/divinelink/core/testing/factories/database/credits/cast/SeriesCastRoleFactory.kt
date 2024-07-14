package com.divinelink.core.testing.factories.database.credits.cast

import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.testing.factories.database.credits.AggregateCreditsFactory

object SeriesCastRoleFactory {

  fun kevinMalone() = SeriesCastRole(
    castId = 94622,
    creditId = "525730a9760ee3776a3447f1",
    character = "Kevin Malone",
    episodeCount = 217,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun angelaMartin() = SeriesCastRole(
    castId = 113867,
    creditId = "525730ab760ee3776a344a0b",
    character = "Angela Martin",
    episodeCount = 210,
    aggregateCreditId = AggregateCreditsFactory.theOffice().id,
  )

  fun allCastRoles() = listOf(
    kevinMalone(),
    angelaMartin(),
  )
}
