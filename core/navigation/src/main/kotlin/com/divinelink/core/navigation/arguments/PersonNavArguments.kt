package com.divinelink.core.navigation.arguments

import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

data class PersonNavArguments(
  val id: Long,
  val knownForDepartment: String?,
  val name: String?,
  val profilePath: String?,
  val gender: Gender?,
)

fun Person.map() = PersonNavArguments(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name,
  profilePath = profilePath,
  gender = gender,
)
