package com.divinelink.core.model.details

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.person.Gender
import kotlinx.serialization.Serializable

@Serializable
data class Person(
  val id: Long,
  val name: String,
  val profilePath: String?,
  val gender: Gender = Gender.NOT_SET,
  val role: PersonRole,
)
