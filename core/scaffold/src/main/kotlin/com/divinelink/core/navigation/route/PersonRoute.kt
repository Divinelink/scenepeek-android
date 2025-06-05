package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender
import kotlinx.serialization.Serializable

@Serializable
data class PersonRoute(
  val id: Long,
  val knownForDepartment: String?,
  val name: String?,
  val profilePath: String?,
  val gender: Gender?,
)

fun Person.map() = PersonRoute(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name,
  profilePath = profilePath,
  gender = gender,
)

fun PersonRoute.map() = Person(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name ?: "",
  profilePath = profilePath,
  gender = gender ?: Gender.NOT_SET,
  role = listOf(PersonRole.Unknown),
)

fun NavController.navigateToPerson(route: PersonRoute) = navigate(route = route)
