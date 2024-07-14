package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.model.CastEntity

object CastEntityFactory {

  fun brianBaumgartner() = CastEntity(
    id = 94622,
    name = "Brian Baumgartner",
    originalName = "Brian Baumgartner",
    knownForDepartment = "Acting",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    totalEpisodeCount = 217,
    character = "Kevin Malone",
  )

  fun angelaKinsey() = CastEntity(
    id = 113867,
    name = "Angela Kinsey",
    originalName = "Angela Kinsey",
    knownForDepartment = "Acting",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
    totalEpisodeCount = 210,
    character = "Angela Martin",
  )

  fun allCast() = listOf(
    brianBaumgartner(),
    angelaKinsey(),
  )
}
