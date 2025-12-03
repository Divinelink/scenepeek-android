package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField

class UpdatePlaceOfBirth(private val repository: PersonRepository) :
  PersonChangeHandler(
    repository = repository,
    field = PersonChangeField.PLACE_OF_BIRTH,
  ) {
  override fun updateField(
    id: Long,
    value: String?,
  ) {
    repository.updatePerson(
      placeOfBirth = value,
      id = id,
    )
  }
}
