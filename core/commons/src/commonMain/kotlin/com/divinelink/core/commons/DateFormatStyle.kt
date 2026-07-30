package com.divinelink.core.commons

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 *  Full:      Τρίτη 10 Δεκεμβρίου 2019 -           Tuesday, December 10, 2019
 *  Long:      8 Δεκεμβρίου 2019        -           December 8, 2019
 *  Medium:    8 Δεκ 2019               -           Dec 8, 2019
 *  Short:     8/12/19                  -           12/8/19
 */
enum class DateFormatStyle { SHORT, MEDIUM, LONG, FULL }

/**
 * MEDIUM_NO_DAY: Δεκ 2019 - Dec 2019
 */
enum class DateFormat(val format: String) {
  MEDIUM_NO_DAY("MMM yyyy"),
}

expect fun LocalDate.formatLocalized(style: DateFormatStyle = DateFormatStyle.MEDIUM): String

expect fun LocalDate.formatLocalized(format: DateFormat): String

expect fun LocalDateTime.formatLocalized(
  dateStyle: DateFormatStyle = DateFormatStyle.MEDIUM,
  timeStyle: DateFormatStyle = DateFormatStyle.SHORT,
): String
