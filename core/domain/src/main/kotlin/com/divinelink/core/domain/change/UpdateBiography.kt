package com.divinelink.core.domain.change

import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

class UpdateBiography(private val personDao: PersonDao) : ChangeHandler {
  override fun execute(items: List<ChangeItem>) {
    items.forEach { item ->
      item.originalValue as? StringValue
      item.value as? StringValue

      println("Updating biography with ${item.originalValue}")
      println("Updating biography with ${item.value}")
    }
  }
}
