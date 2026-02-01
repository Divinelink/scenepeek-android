package com.divinelink.core.testing.factories.entity.person

import com.divinelink.core.database.credits.ShowCastRoleEntity

object ShowCastRoleEntityFactory {

  val kevinMalone = ShowCastRoleEntity(
    showId = 2316,
    creditId = "525730a9760ee3776a3447f1",
    episodeCount = 217,
    creditOrder = 16,
  )

  val angelaMartin = ShowCastRoleEntity(
    showId = 2316,
    creditId = "525730ab760ee3776a344a0b",
    episodeCount = 210,
    creditOrder = 20,
  )
}
