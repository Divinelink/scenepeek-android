package com.divinelink.core.domain.change

import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

class UpdateHomepage(private val repository: PersonRepository) : ChangeHandler {
  override fun execute(
    id: Long,
    items: List<ChangeItem>,
  ) {
    val latestUpdate = items.last()
    val value = latestUpdate.value as? StringValue

    repository.updatePerson(
      homepage = value?.value,
      id = id,
    )
  }
}
