package com.divinelink.core.ui.components.expandablefab

import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.ui.UIText

open class FloatingActionButtonItem(
  val icon: ImageVector,
  val label: UIText,
  val contentDescription: UIText,
  val onClick: () -> Unit,
)
