package com.divinelink.core.ui.extension

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Double?.getColorRating(): Color = when (this) {
  null -> MaterialTheme.colorScheme.onSurface
  in 0.1..3.5 -> Color(0xFFDB2360)
  in 3.5..6.99 -> Color(0xFFD2D531)
  in 7.0..10.0 -> Color(0xFF21D07A)
  else -> MaterialTheme.colorScheme.onSurface
}
