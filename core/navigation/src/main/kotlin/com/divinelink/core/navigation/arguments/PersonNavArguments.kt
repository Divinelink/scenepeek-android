package com.divinelink.core.navigation.arguments

import com.divinelink.core.model.credits.PersonRole
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

fun PersonNavArguments.map() = Person(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name ?: "",
  profilePath = profilePath,
  gender = gender ?: Gender.NOT_SET,
  role = listOf(PersonRole.Unknown),
)
