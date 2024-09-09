package com.divinelink.core.domain.change.person

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.database.person.PersonChangeField
import com.divinelink.core.domain.change.ChangeHandler
import com.divinelink.core.model.change.ChangeAction
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

/**
 * Abstracted common delete/update logic for all person fields into this class,
 * making it responsible for handling changes in the database.
 */
abstract class PersonChangeHandler(
  private val repository: PersonRepository,
  private val field: PersonChangeField,
) : ChangeHandler {

  abstract fun updateField(
    id: Long,
    value: String?,
  )

  override fun execute(
    id: Long,
    items: List<ChangeItem>,
  ) {
    val latestUpdate = items.last()
    val value = latestUpdate.value as? StringValue

    if (latestUpdate.action == ChangeAction.DELETED) {
      repository.deleteFromPerson(id, field)
    } else {
      updateField(id, value?.value)
    }
  }
}
