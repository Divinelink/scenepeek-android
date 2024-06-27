package com.divinelink.core.commons.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatTo(
  inputFormat: String,
  outputFormat: String,
): String? {
  val input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
  val output = SimpleDateFormat(outputFormat, Locale.ENGLISH)
  val date = input.parse(this)
  return date?.let { output.format(it) }
}
