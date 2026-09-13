package com.divinelink.core.ui.components.action

import com.divinelink.core.model.UIText
import com.divinelink.core.ui.IconWrapper

open class MediaActionButtonItem(
  val icon: IconWrapper,
  val label: UIText,
  val seerrMode: Boolean = false,
  val onClick: () -> Unit,
)
