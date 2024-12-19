package com.divinelink.core.commons.extensions

import kotlin.math.round

@Suppress("MagicNumber")
fun Double.round(decimals: Int): Double {
  var multiplier = 1.0
  repeat(decimals) { multiplier *= 10 }
  return round(this * multiplier) / multiplier
}
