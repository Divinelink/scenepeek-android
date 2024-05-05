@file:Suppress("MagicNumber")

package com.andreolas.movierama.ui

import androidx.compose.ui.graphics.Color

// TODO Re-package this extensions file to the appropriate package

fun String.getColorRating(): Color {
  return when (toDouble()) {
    in 0.1..3.5 -> Color(0xFFDB2360)
    in 3.5..7.0 -> Color(0xFFD2D531)
    in 7.0..10.0 -> Color(0xFF21D07A)
    else -> Color.White
  }
}
