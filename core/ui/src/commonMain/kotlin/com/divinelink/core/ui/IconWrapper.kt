package com.divinelink.core.ui

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed interface IconWrapper {
  data class Icon(val resourceId: DrawableResource) : IconWrapper
  data class Image(val resourceId: DrawableResource) : IconWrapper
  data class Vector(val vector: ImageVector) : IconWrapper
}
