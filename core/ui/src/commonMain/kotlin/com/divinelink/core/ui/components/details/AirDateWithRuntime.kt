package com.divinelink.core.ui.components.details

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.divinelink.core.commons.DateFormatStyle
import com.divinelink.core.commons.formatLocalized
import kotlinx.datetime.LocalDate

@Composable
fun AirDateWithRuntime(
  airDate: LocalDate?,
  runtime: String?,
  style: TextStyle,
) {
  airDate?.formatLocalized(DateFormatStyle.LONG)?.let { airDate ->
    Text(
      text = buildString {
        append(airDate)

        runtime?.let { runtime ->
          append(" • $runtime")
        }
      },
      style = style,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
