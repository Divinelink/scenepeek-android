package com.divinelink.core.ui.components.expandablefab

import androidx.compose.ui.graphics.vector.ImageVector

data class FloatingActionButtonItem(
  val icon: ImageVector,
  val label: String,
  val contentDescription: String,
  val onClick: () -> Unit,
)
