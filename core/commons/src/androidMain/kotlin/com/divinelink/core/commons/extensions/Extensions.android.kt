package com.divinelink.core.commons.extensions

import java.text.DecimalFormat

actual fun Int.formatWithCommas(): String {
  return DecimalFormat("#,###").format(this)
}

actual fun Long.formatWithCommas(): String {
  return DecimalFormat("#,###").format(this)
}
