package com.divinelink.core.ui.components.expandablefab

import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.UIText

open class FloatingActionButtonItem(
  val icon: IconWrapper,
  val label: UIText,
  val contentDescription: UIText,
  val onClick: () -> Unit,
)
