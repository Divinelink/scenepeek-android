package com.divinelink.core.commons

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

enum class DateFormatStyle { SHORT, MEDIUM, LONG, FULL }

/**
 *  Full: Τρίτη 10 Δεκεμβρίου 2019 -
 *  Long: 8 Δεκεμβρίου 2019 -
 *  Medium: 8 Δεκ 2019 -
 *  Short: 8/12/19 -
 */

expect fun LocalDate.formatLocalized(style: DateFormatStyle = DateFormatStyle.MEDIUM): String

expect fun LocalDateTime.formatLocalized(
  dateStyle: DateFormatStyle = DateFormatStyle.MEDIUM,
  timeStyle: DateFormatStyle = DateFormatStyle.SHORT,
): String
