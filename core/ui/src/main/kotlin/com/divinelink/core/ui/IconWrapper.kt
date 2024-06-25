package com.divinelink.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconWrapper {
  data class Icon(@DrawableRes val resourceId: Int) : IconWrapper
  data class Image(@DrawableRes val resourceId: Int) : IconWrapper
  data class Vector(val vector: ImageVector) : IconWrapper
}
