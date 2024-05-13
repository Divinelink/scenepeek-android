package com.andreolas.movierama.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val MaterialCardShape = RoundedCornerShape(16.dp)

val SearchBarShape = RoundedCornerShape(50.dp)

// Popular Movies
val PopularMovieItemShape = RoundedCornerShape(8.dp)
val MovieImageShape = RoundedCornerShape(8.dp)

@Immutable
data class Shapes(
  val roundedShape: RoundedCornerShape = RoundedCornerShape(50.dp)
)

val MaterialTheme.shape: Shapes
  @Composable
  @ReadOnlyComposable
  get() = LocalShapes.current

internal val LocalShapes = staticCompositionLocalOf { Shapes() }
