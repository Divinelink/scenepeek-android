package com.divinelink.core.navigation.arguments

import com.divinelink.core.model.details.Person

data class PersonNavArguments(
  val id: Long,
  val knownForDepartment: String?,
  val name: String?,
  val profilePath: String?,
)

fun Person.map() = PersonNavArguments(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name,
  profilePath = profilePath,
)
