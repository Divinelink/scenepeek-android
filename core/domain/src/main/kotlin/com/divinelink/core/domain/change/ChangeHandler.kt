package com.divinelink.core.domain.change

import com.divinelink.core.model.change.ChangeItem

interface ChangeHandler {
  fun execute(items: List<ChangeItem>)
}
