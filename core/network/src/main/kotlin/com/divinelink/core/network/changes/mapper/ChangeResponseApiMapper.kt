package com.divinelink.core.network.changes.mapper

import com.divinelink.core.model.change.Change
import com.divinelink.core.model.change.ChangeAction
import com.divinelink.core.model.change.ChangeItem
import com.divinelink.core.model.change.ChangeType
import com.divinelink.core.model.change.Changes
import com.divinelink.core.network.changes.model.api.ChangeApi
import com.divinelink.core.network.changes.model.api.ChangeItemApi
import com.divinelink.core.network.changes.model.api.ChangesResponseApi

fun ChangesResponseApi.map() = Changes(
  changes = changes.map { it.map() },
)

fun ChangeApi.map() = Change(
  key = ChangeType.from(key),
  items = items.map { it.map() },
)

fun ChangeItemApi.map() = ChangeItem(
  id = id,
  action = ChangeAction.from(action),
  time = time,
  iso6391 = iso6391,
  iso31661 = iso31661,
  value = value,
  originalValue = originalValue,
)
