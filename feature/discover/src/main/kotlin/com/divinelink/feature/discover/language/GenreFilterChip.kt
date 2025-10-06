package com.divinelink.feature.discover.language

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.divinelink.core.model.locale.Language
import com.divinelink.core.ui.UiString

@Composable
fun LanguageFilterChip(
  filters: List<Language>,
  onClick: () -> Unit,
) {
  FilterChip(
    selected = filters.isNotEmpty(),
    label = {
      val text = when {
        filters.isEmpty() -> stringResource(UiString.core_ui_languages)
        filters.size == 1 -> stringResource(filters.first().nameRes)
        else -> buildString {
          append(stringResource(filters.first().nameRes))
          append("+")
          append(filters.size - 1)
        }
      }

      Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
      )
    },
    trailingIcon = {
      Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = null,
      )
    },
    onClick = onClick,
  )
}
