package com.divinelink.core.model.filter

import com.divinelink.core.model.UIText

sealed class SelectableFilter(
  open val value: String,
  open val name: UIText,
  open val isSelected: Boolean,
)
