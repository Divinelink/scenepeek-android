package com.divinelink.core.model.tab

sealed class Tab(
  open val order: Int,
  open val value: String,
  open val titleRes: Int,
)
