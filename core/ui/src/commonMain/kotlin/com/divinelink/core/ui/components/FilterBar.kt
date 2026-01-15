package com.divinelink.core.ui.components

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.filter.HomeFilter
import com.divinelink.core.model.filter.SelectableFilter
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.getString

const val FILTER_BAR_TEST_TAG = "FILTER_BAR"

@Composable
fun FilterBar(
  modifier: Modifier = Modifier,
  filters: List<SelectableFilter>,
  onFilterClick: (SelectableFilter) -> Unit,
  onClearClick: () -> Unit,
) {
  if (filters.isEmpty()) return

  CompositionLocalProvider(
    LocalOverscrollFactory provides null,
  ) {
    LazyRow(
      modifier = modifier
        .testTag(FILTER_BAR_TEST_TAG)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val sortedFilters = filters.sortedBy { !it.isSelected }

      clearButton(
        isVisible = sortedFilters.any { it.isSelected },
        onClearClick = onClearClick,
      )

      items(
        count = sortedFilters.size,
        key = { index -> sortedFilters[index].value },
      ) { index ->
        val filter = sortedFilters[index]
        FilterItem(
          modifier = Modifier.animateItem(),
          filter = filter,
          onFilterClick = onFilterClick,
        )
      }
    }
  }
}

@Composable
private fun FilterItem(
  modifier: Modifier = Modifier,
  filter: SelectableFilter,
  onFilterClick: (SelectableFilter) -> Unit,
) {
  val color = if (filter.isSelected) {
    MaterialTheme.colorScheme.primary
  } else {
    MaterialTheme.colorScheme.surfaceContainerHighest
  }
  Button(
    colors = ButtonDefaults.buttonColors(
      containerColor = color,
      contentColor = contentColorFor(color),
    ),
    modifier = modifier,
    onClick = { onFilterClick(filter) },
  ) {
    Text(text = filter.name.getString())
  }
}

@Previews
@Composable
private fun FilterBarPreview() {
  AppTheme {
    FilterBar(
      filters = listOf(
        HomeFilter.Favorites(false),
        HomeFilter.TopRated(true),
      ),
      onFilterClick = { },
      onClearClick = { },
    )
  }
}

@Previews
@Composable
private fun FilterBarUnselectedPreview() {
  AppTheme {
    val filters = mutableListOf(
      HomeFilter.Favorites(false),
      HomeFilter.TopRated(false),
    )
    FilterBar(
      filters = filters,
      onFilterClick = { },
      onClearClick = { },
    )
  }
}
