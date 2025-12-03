package com.divinelink.core.model.tab

import org.jetbrains.compose.resources.StringResource

sealed class Tab(
  open val order: Int,
  open val value: String,
  open val titleRes: StringResource,
)
