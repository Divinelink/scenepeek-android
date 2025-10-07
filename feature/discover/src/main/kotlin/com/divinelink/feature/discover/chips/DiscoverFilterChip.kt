package com.divinelink.feature.discover.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.model.Genre
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.ui.UiString

object DiscoverFilterChip {

  @Composable
  fun Genre(
    filters: List<Genre>,
    onClick: () -> Unit,
  ) {
    Chip(
      selected = filters.isNotEmpty(),
      label = when {
        filters.isEmpty() -> stringResource(UiString.core_ui_genres)
        filters.size == 1 -> filters.first().name
        else -> buildString {
          append(filters.first().name)
          append("+")
          append(filters.size - 1)
        }
      },
      onClick = onClick,
    )
  }

  @Composable
  fun Language(
    language: Language?,
    onClick: () -> Unit,
  ) {
    Chip(
      selected = language != null,
      label = if (language == null) {
        stringResource(UiString.core_ui_language)
      } else {
        stringResource(language.nameRes)
      },
      onClick = onClick,
    )
  }

  @Composable
  fun Country(
    country: Country?,
    onClick: () -> Unit,
  ) {
    Chip(
      selected = country != null,
      label = if (country == null) {
        stringResource(UiString.core_ui_country)
      } else {
        stringResource(country.nameRes) + "  ${country.flag}"
      },
      onClick = onClick,
    )
  }

  @Composable
  private fun Chip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
  ) {
    FilterChip(
      selected = selected,
      label = {
        Text(
          text = label,
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
      modifier = modifier,
    )
  }
}
