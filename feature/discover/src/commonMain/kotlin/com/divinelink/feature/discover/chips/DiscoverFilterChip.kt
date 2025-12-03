package com.divinelink.feature.discover.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.model.Genre
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.locale.Country
import com.divinelink.core.model.locale.Language
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_country
import com.divinelink.core.ui.resources.core_ui_genres
import com.divinelink.core.ui.resources.core_ui_language
import com.divinelink.core.ui.resources.core_ui_rating
import com.divinelink.core.ui.resources.core_ui_rating_selected
import org.jetbrains.compose.resources.stringResource

object DiscoverFilterChip {

  @Composable
  fun Genre(
    modifier: Modifier,
    filters: List<Genre>,
    onClick: () -> Unit,
  ) {
    Chip(
      modifier = modifier,
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
    modifier: Modifier,
    language: Language?,
    onClick: () -> Unit,
  ) {
    Chip(
      modifier = modifier,
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
    modifier: Modifier,
    country: Country?,
    onClick: () -> Unit,
  ) {
    Chip(
      modifier = modifier,
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
  fun VoteAverage(
    modifier: Modifier,
    votes: Int?,
    voteAverage: DiscoverFilter.VoteAverage?,
    onClick: () -> Unit,
  ) {
    Chip(
      modifier = modifier,
      selected = voteAverage != null || votes != null,
      label = if (voteAverage == null) {
        stringResource(UiString.core_ui_rating)
      } else {
        stringResource(
          UiString.core_ui_rating_selected,
          voteAverage.greaterThan,
          voteAverage.lessThan,
        )
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
