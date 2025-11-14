package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField

class UpdateHomepage(private val repository: PersonRepository) :
  PersonChangeHandler(
    repository = repository,
    field = PersonChangeField.HOMEPAGE,
  ) {
  override fun updateField(
    id: Long,
    value: String?,
  ) {
    repository.updatePerson(
      homepage = value,
      id = id,
    )
  }
}
