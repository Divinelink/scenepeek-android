package com.divinelink.core.testing.factories.entity.person

import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.model.person.Gender

object PersonEntityFactory {

  val brianBaumgartner = PersonEntity(
    id = 94622,
    name = "Brian Baumgartner",
    originalName = "Brian Baumgartner",
    profilePath = "/1O7ECkD4mOKAgMAbQADBpTKBzOP.jpg",
    gender = Gender.MALE.value.toLong(),
    knownForDepartment = "Acting",
  )

  val angelaKinsey = PersonEntity(
    id = 113867,
    name = "Angela Kinsey",
    originalName = "Angela Kinsey",
    profilePath = "/qHFidnMcFqUWdMFyjmXufBlYckd.jpg",
    knownForDepartment = "Acting",
    gender = Gender.FEMALE.value.toLong(),
  )

  val officeCast = listOf(
    brianBaumgartner,
    angelaKinsey,
  )
}
