package com.divinelink.core.ui.extension

import androidx.compose.runtime.Composable
import com.divinelink.core.model.locale.Month
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_localized_date_full
import com.divinelink.core.ui.resources.core_ui_localized_month_year
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocalDateTime?.localizeIsoDate(useLong: Boolean = true): String? = this?.let {
  stringResource(
    UiString.core_ui_localized_date_full,
    stringResource(
      Month.from(month.number).run {
        if (useLong) long else short
      },
    ),
    day,
    year,
  )
}

@Composable
fun LocalDate?.localizeMonthYear(useLong: Boolean = true): String? = this?.let {
  stringResource(
    UiString.core_ui_localized_month_year,
    stringResource(
      Month.from(month.number).run { if (useLong) long else short },
    ),
    year,
  )
}

@Composable
fun LocalDate?.localizeFull(useLong: Boolean = true): String? = this?.let {
  stringResource(
    UiString.core_ui_localized_date_full,
    stringResource(
      Month.from(month.number).run { if (useLong) long else short },
    ),
    day,
    year,
  )
}
