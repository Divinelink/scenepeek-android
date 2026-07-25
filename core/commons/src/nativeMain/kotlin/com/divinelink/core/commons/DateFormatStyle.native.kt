package com.divinelink.core.commons

import androidx.compose.ui.text.intl.Locale
import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun LocalDate.formatLocalized(style: DateFormatStyle): String {
  val components = NSDateComponents().apply {
    year = this@formatLocalized.year.toLong()
    month = this@formatLocalized.month.number.toLong()
    day = this@formatLocalized.day.toLong()
  }
  val date = NSCalendar.currentCalendar.dateFromComponents(components)!!
  val formatter = NSDateFormatter().apply {
    this.dateStyle = style.toNSStyle()
    this.locale = NSLocale(Locale.current.language)
  }
  return formatter.stringFromDate(date)
}

actual fun LocalDateTime.formatLocalized(
  dateStyle: DateFormatStyle,
  timeStyle: DateFormatStyle,
): String {
  val components = NSDateComponents().apply {
    year = this@formatLocalized.year.toLong()
    month = this@formatLocalized.month.number.toLong()
    day = this@formatLocalized.day.toLong()
    hour = this@formatLocalized.hour.toLong()
    minute = this@formatLocalized.minute.toLong()
    second = this@formatLocalized.second.toLong()
  }
  val date = NSCalendar.currentCalendar.dateFromComponents(components)!!
  val formatter = NSDateFormatter().apply {
    this.dateStyle = dateStyle.toNSStyle()
    this.timeStyle = timeStyle.toNSStyle()
    this.locale = NSLocale(Locale.current.language)
  }
  return formatter.stringFromDate(date)
}

private fun DateFormatStyle.toNSStyle() = when (this) {
  DateFormatStyle.SHORT -> NSDateFormatterShortStyle
  DateFormatStyle.MEDIUM -> NSDateFormatterMediumStyle
  DateFormatStyle.LONG -> NSDateFormatterLongStyle
  DateFormatStyle.FULL -> NSDateFormatterFullStyle
}
