package com.divinelink.ui.extension

import androidx.compose.ui.graphics.Color

fun String.getColorRating(): Color {
  return when (toDouble()) {
    in 0.1..3.5 -> Color(0xFFDB2360)
    in 3.5..6.99 -> Color(0xFFD2D531)
    in 7.0..10.0 -> Color(0xFF21D07A)
    else -> Color.White
  }
}
