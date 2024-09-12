package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField

class UpdateImdbID(private val repository: PersonRepository) :
  PersonChangeHandler(repository = repository, field = PersonChangeField.IMDB_ID) {
  override fun updateField(
    id: Long,
    value: String?,
  ) {
    repository.updatePerson(
      imdbId = value,
      id = id,
    )
  }
}
