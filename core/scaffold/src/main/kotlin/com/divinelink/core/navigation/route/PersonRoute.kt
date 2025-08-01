package com.divinelink.core.navigation.route

import androidx.navigation.NavController
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.person.Gender

fun Person.toPersonRoute() = Navigation.PersonRoute(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name,
  profilePath = profilePath,
  gender = gender,
)

fun Navigation.PersonRoute.map() = Person(
  id = id,
  knownForDepartment = knownForDepartment,
  name = name ?: "",
  profilePath = profilePath,
  gender = gender ?: Gender.NOT_SET,
  role = listOf(PersonRole.Unknown),
)

fun NavController.navigateToPerson(route: Navigation.PersonRoute) = navigate(route = route)
