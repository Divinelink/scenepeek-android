package com.divinelink.core.testing.factories.entity.person

import com.divinelink.core.database.cast.PersonRoleEntity

object PersonRoleEntityFactory {

  val kevinMalone = PersonRoleEntity(
    character = "Kevin Malone",
    creditId = "525730a9760ee3776a3447f1",
    castId = 94622,
  )

  val angelaMartin = PersonRoleEntity(
    character = "Angela Martin",
    creditId = "525730ab760ee3776a344a0b",
    castId = 113867,
  )
}
