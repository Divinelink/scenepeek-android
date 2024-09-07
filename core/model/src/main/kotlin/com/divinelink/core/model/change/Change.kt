package com.divinelink.core.model.change

data class Change(
  val key: ChangeType,
  val items: List<ChangeItem>,
)
