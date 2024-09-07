package com.divinelink.core.domain.change

import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.StringValue

class UpdateName : ChangeHandler {
  override fun execute(items: List<ChangeItem>) {
    items.forEach { item ->
      item.originalValue as? StringValue
      item.value as? StringValue

      println("Updating name with ${item.value}")
      println("Updating name with ${item.originalValue}")
    }
  }
}
