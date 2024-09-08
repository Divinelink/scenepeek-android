package com.divinelink.core.domain.change

import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.ChangeType

class PersonChangesActionFactory(personDao: PersonDao) {

  private val actionMap: Map<ChangeType, ChangeHandler> = mapOf(
    ChangeType.BIOGRAPHY to UpdateBiography(personDao),
    ChangeType.NAME to UpdateName(personDao),
    ChangeType.IMDB_ID to UpdateImdbID(personDao),
    ChangeType.BIRTHDAY to UpdateBirthday(personDao),
    ChangeType.DAY_OF_DEATH to UpdateDayOfDeath(personDao),
    ChangeType.GENDER to UpdateGender(personDao),
    ChangeType.PLACE_OF_BIRTH to UpdatePlaceOfBirth(personDao),
  )

  fun getAction(key: ChangeType): ChangeHandler? = actionMap[key]
}
