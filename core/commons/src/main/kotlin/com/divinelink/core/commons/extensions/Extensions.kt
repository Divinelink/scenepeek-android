package com.divinelink.core.commons.extensions

import kotlin.math.round

@Suppress("MagicNumber")
fun Double.round(decimals: Int): Double {
  var multiplier = 1.0
  repeat(decimals) { multiplier *= 10 }
  return round(this * multiplier) / multiplier
}

fun Double.isWholeNumber(): Boolean = this % 1.0 == 0.0

fun Int.toShortString(): String {
  return when {
    this >= 1_000_000 -> {
      val millions = this / 1_000_000
      val thousands = (this % 1_000_000) / 100_000
      if (thousands > 0) {
        "$millions.${thousands}m"
      } else {
        "${millions}m"
      }
    }
    this >= 1_000 -> {
      val thousands = this / 1_000
      val remainder = (this % 1_000) / 100
      if (remainder > 0) {
        "$thousands.${remainder}k"
      } else {
        "${thousands}k"
      }
    }
    else -> this.toString()
  }
}

