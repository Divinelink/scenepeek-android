package com.divinelink.core.commons.extensions

import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round

@Suppress("MagicNumber")
fun Double.round(decimals: Int): Double {
  var multiplier = 1.0
  repeat(decimals) { multiplier *= 10 }
  return round(this * multiplier) / multiplier
}

fun Double.isWholeNumber(): Boolean = this % 1.0 == 0.0

fun Int.toShortString(): String = when {
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

/**
 * Converts a byte count (as Long) to a human-readable string with appropriate unit suffix.
 * Units used: B, KB, MB, GB, TB, PB, EB, ZB, YB
 * @param decimalPlaces Number of decimal places to show (default is 2)
 * @return Formatted string like "1.50 KB", "2.34 GB", etc.
 */
fun Long.bytesToHumanReadable(decimalPlaces: Int = 2): String {
  if (this == 0L) return "0 B"
  if (this < 0) return "-${(-this).bytesToHumanReadable(decimalPlaces)}"

  val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
  val base = 1024.0

  val unitIndex = minOf((ln(this.toDouble()) / ln(base)).toInt(), units.size - 1)

  val value = this.toDouble() / (base.pow(unitIndex))

  // Round to specified decimal places
  val multiplier = 10.0.pow(decimalPlaces)
  val rounded = (round(value * multiplier) / multiplier)

  return "$rounded ${units[unitIndex]}"
}
