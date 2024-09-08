package com.divinelink.core.domain.change

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.change.ChangeType

class PersonChangesActionFactory(repository: PersonRepository) {
  private val actionMap: Map<ChangeType, ChangeHandler> = mapOf(
    ChangeType.BIOGRAPHY to UpdateBiography(repository),
    ChangeType.NAME to UpdateName(repository),
    ChangeType.IMDB_ID to UpdateImdbID(repository),
    ChangeType.BIRTHDAY to UpdateBirthday(repository),
    ChangeType.DAY_OF_DEATH to UpdateDayOfDeath(repository),
    ChangeType.GENDER to UpdateGender(repository),
    ChangeType.PLACE_OF_BIRTH to UpdatePlaceOfBirth(repository),
    ChangeType.HOMEPAGE to UpdateHomepage(repository),
  )

  fun getAction(key: ChangeType): ChangeHandler? = actionMap[key]
}
