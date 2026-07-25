package com.divinelink.core.commons

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

actual fun LocalDate.formatLocalized(style: DateFormatStyle): String {
  val formatter = DateTimeFormatter
    .ofLocalizedDate(style.toJavaStyle())
    .withLocale(Locale.getDefault())
  return toJavaLocalDate().format(formatter)
}

actual fun LocalDateTime.formatLocalized(
  dateStyle: DateFormatStyle,
  timeStyle: DateFormatStyle,
): String {
  val formatter = DateTimeFormatter
    .ofLocalizedDateTime(dateStyle.toJavaStyle(), timeStyle.toJavaStyle())
    .withLocale(Locale.getDefault())
  return toJavaLocalDateTime().format(formatter)
}

private fun DateFormatStyle.toJavaStyle() = when (this) {
  DateFormatStyle.SHORT -> FormatStyle.SHORT
  DateFormatStyle.MEDIUM -> FormatStyle.MEDIUM
  DateFormatStyle.LONG -> FormatStyle.LONG
  DateFormatStyle.FULL -> FormatStyle.FULL
}
