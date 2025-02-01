package com.divinelink.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val SearchBarShape = RoundedCornerShape(50.dp)

@Immutable
data class Shapes(
  val extraSmall: RoundedCornerShape = RoundedCornerShape(2.dp),
  val small: RoundedCornerShape = RoundedCornerShape(4.dp),
  val medium: RoundedCornerShape = RoundedCornerShape(8.dp),
  val large: RoundedCornerShape = RoundedCornerShape(16.dp),
  val rounded: RoundedCornerShape = RoundedCornerShape(50.dp),
)

val MaterialTheme.shape: Shapes
  @Composable
  @ReadOnlyComposable
  get() = LocalShapes.current

internal val LocalShapes = staticCompositionLocalOf { Shapes() }
