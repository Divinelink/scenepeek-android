package com.divinelink.core.ui.extension

import androidx.compose.runtime.Composable
import com.divinelink.core.model.locale.Month
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_localized_date_full
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocalDateTime?.localizeIsoDate(): String? = this?.let {
  stringResource(
    UiString.core_ui_localized_date_full,
    stringResource(Month.from(month.number).resource),
    day,
    year,
  )
}
