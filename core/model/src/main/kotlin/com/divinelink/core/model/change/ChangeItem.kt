package com.divinelink.core.model.change

data class ChangeItem(
  val id: String,
  val action: ChangeAction,
  val time: String,
  val iso6391: String,
  val iso31661: String,
  val value: ChangeValue?,
  val originalValue: ChangeValue?,
)
