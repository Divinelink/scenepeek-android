package com.divinelink.core.model.filter

import com.divinelink.core.model.UIText

sealed class Filter(
  open val value: String,
  open val name: UIText,
)
