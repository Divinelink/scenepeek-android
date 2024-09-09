package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField

class UpdateBirthday(private val repository: PersonRepository) :
  PersonChangeHandler(
    repository = repository,
    field = PersonChangeField.BIRTHDAY,
  ) {
  override fun updateField(
    id: Long,
    value: String?,
  ) {
    repository.updatePerson(
      birthday = value,
      id = id,
    )
  }
}
