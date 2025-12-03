package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.model.CastEntity

object CastEntityFactory {

  fun brianBaumgartner() = CastEntity(
    id = 94622,
    name = "Brian Baumgartner",
    originalName = "Brian Baumgartner",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    gender = 2,
    knownForDepartment = "Acting",
    roles = listOf(
      SeriesCastRole(
        character = "Kevin Malone",
        creditId = "525730a9760ee3776a3447f1",
        episodeCount = 217,
        castId = 94622,
        aggregateCreditId = 2316,
      ),
    ),
  )

  fun angelaKinsey() = CastEntity(
    id = 113867,
    name = "Angela Kinsey",
    originalName = "Angela Kinsey",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
    knownForDepartment = "Acting",
    gender = 1,
    roles = listOf(
      SeriesCastRole(
        character = "Angela Martin",
        creditId = "525730ab760ee3776a344a0b",
        episodeCount = 210,
        castId = 113867,
        aggregateCreditId = 2316,
      ),
    ),
  )

  fun allCast() = listOf(
    brianBaumgartner(),
    angelaKinsey(),
  )
}
