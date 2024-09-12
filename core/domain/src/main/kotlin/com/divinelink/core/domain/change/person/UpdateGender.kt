package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField

class UpdateGender(private val repository: PersonRepository) :
  PersonChangeHandler(
    repository = repository,
    field = PersonChangeField.GENDER,
  ) {
  override fun updateField(
    id: Long,
    value: String?,
  ) {
    repository.updatePerson(
      gender = value?.toInt(),
      id = id,
    )
  }
}
