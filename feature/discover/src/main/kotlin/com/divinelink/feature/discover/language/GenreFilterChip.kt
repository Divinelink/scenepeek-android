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
  language: Language?,
  onClick: () -> Unit,
) {
  FilterChip(
    selected = language != null,
    label = {
      val text = if (language == null) {
        stringResource(UiString.core_ui_language)
      } else {
        stringResource(language.nameRes)
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
