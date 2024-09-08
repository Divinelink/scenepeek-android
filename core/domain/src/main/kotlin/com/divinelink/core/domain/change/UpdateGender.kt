package com.divinelink.core.domain.change

import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

class UpdateGender(private val personDao: PersonDao) : ChangeHandler {
  override fun execute(
    id: Long,
    items: List<ChangeItem>,
  ) {
    val latestUpdate = items.last()
    val value = latestUpdate.value as? StringValue

    personDao.updatePerson(
      gender = value?.value?.toInt(),
      id = id,
    )
  }
}
