package com.divinelink.core.testing.factories.entity.credits

import com.divinelink.core.database.credits.model.CastEntity

object CastEntityFactory {

  fun brianBaumgartner() = CastEntity(
    id = 94622,
    name = "Brian Baumgartner",
    originalName = "Brian Baumgartner",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    totalEpisodeCount = 217,
    character = "Kevin Malone",
    creditId = "525730a9760ee3776a3447f1",
  )

  fun angelaKinsey() = CastEntity(
    id = 113867,
    name = "Angela Kinsey",
    originalName = "Angela Kinsey",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
    totalEpisodeCount = 210,
    character = "Angela Martin",
    creditId = "525730ab760ee3776a344a0b",
  )

  fun allCast() = listOf(
    brianBaumgartner(),
    angelaKinsey(),
  )
}
