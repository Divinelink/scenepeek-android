package com.divinelink.feature.request.media.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun JellyseerrGradientText(
  text: String,
  modifier: Modifier = Modifier,
) {
  val gradient = Brush.linearGradient(
    colors = listOf(
      Color(0xFF818cf8),
      Color(0xFFc084fc),
    ),
    start = Offset.Zero,
    end = Offset.Infinite,
  )

  Text(
    text = text,
    modifier = modifier,
    style = MaterialTheme.typography.headlineSmall.copy(
      brush = gradient,
      fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
    ),
  )
}
