package com.divinelink.core.domain.change

import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.ChangeType

class PersonChangesActionFactory(personDao: PersonDao) {

  private val actionMap: Map<ChangeType, ChangeHandler> = mapOf(
    ChangeType.BIOGRAPHY to UpdateBiography(personDao),
    ChangeType.NAME to UpdateName(),
  )

  fun getAction(key: ChangeType): ChangeHandler? = actionMap[key]
}
