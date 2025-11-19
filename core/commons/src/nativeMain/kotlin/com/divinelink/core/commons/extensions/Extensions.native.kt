package com.divinelink.core.commons.extensions

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Int.formatWithCommas(): String {
  val formatter = NSNumberFormatter()
  formatter.numberStyle = NSNumberFormatterDecimalStyle
  formatter.groupingSeparator = ","
  return formatter.stringFromNumber(NSNumber(int = this)) ?: this.toString()
}

actual fun Long.formatWithCommas(): String {
  val formatter = NSNumberFormatter()
  formatter.numberStyle = NSNumberFormatterDecimalStyle
  formatter.groupingSeparator = ","
  return formatter.stringFromNumber(NSNumber(longLong = this)) ?: this.toString()
}
