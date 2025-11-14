@file:Suppress("MagicNumber")

package com.divinelink.core.ui.components

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews

/**
 * A composable that will display a list of language.
 *
 * @param modifier The modifier to be applied to the filter bar.
 * @param language The list of language to display.
 * @param onFilterClick A callback that will be called when a filter is clicked.
 * @param onClearClick A callback that will be called when the clear button is clicked.
 */

const val FILTER_BAR_TEST_TAG = "FILTER_BAR"

@Composable
fun FilterBar(
  modifier: Modifier = Modifier,
  filters: List<Filter>,
  onFilterClick: (Filter) -> Unit,
  onClearClick: () -> Unit,
) {
  CompositionLocalProvider(
    LocalOverscrollFactory provides null,
  ) {
    LazyRow(
      modifier = modifier
        .testTag(FILTER_BAR_TEST_TAG)
        .padding(start = 0.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val sortedFilters = filters.sortedBy { !it.isSelected }

      clearButton(
        isVisible = sortedFilters.any { it.isSelected },
        onClearClick = onClearClick,
      )

      items(
        count = sortedFilters.size,
        key = { index -> sortedFilters[index].name },
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
  filter: Filter,
  onFilterClick: (Filter) -> Unit,
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
    Text(text = filter.name)
  }
}

@Previews
@Composable
private fun FilterBarPreview() {
  AppTheme {
    FilterBar(
      filters = listOf(
        Filter("All", true),
        Filter("Popular", false),
        Filter("Top Rated", true),
        Filter("Upcoming", false),
      ),
      onFilterClick = { },
      onClearClick = { },
    )
  }
}

/**
 * A data class that represents a filter.
 * @param name The name of the filter.
 * @param isSelected Whether the filter is selected or not.
 */
data class Filter(
  val name: String,
  val isSelected: Boolean,
)

@Previews
@Composable
private fun FilterBarUnselectedPreview() {
  AppTheme {
    val filters = mutableListOf(
      Filter("All", false),
      Filter("Popular", false),
      Filter("Top Rated", false),
      Filter("Upcoming", false),
    )
    FilterBar(
      filters = filters,
      onFilterClick = { },
      onClearClick = { },
    )
  }
}
