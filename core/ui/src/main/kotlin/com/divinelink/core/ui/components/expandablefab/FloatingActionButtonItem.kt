package com.divinelink.core.ui.components.expandablefab

import com.divinelink.core.model.UIText
import com.divinelink.core.ui.IconWrapper

open class FloatingActionButtonItem(
  val icon: IconWrapper,
  val label: UIText,
  val contentDescription: UIText,
  val onClick: () -> Unit,
)
