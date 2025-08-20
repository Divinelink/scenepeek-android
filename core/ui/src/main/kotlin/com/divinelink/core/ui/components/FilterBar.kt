@file:Suppress("MagicNumber")

package com.divinelink.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

/**
 * A composable that will display a list of filters.
 *
 * @param modifier The modifier to be applied to the filter bar.
 * @param filters The list of filters to display.
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
      item {
        AnimatedVisibility(
          enter = slideInHorizontally() + expandHorizontally(),
          exit = slideOutHorizontally() + shrinkHorizontally(),
          visible = sortedFilters.any { it.isSelected },
        ) {
          ClearButton(
            onClearClick = onClearClick,
          )
        }
      }

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

/**
 * A composable that will display a clear button.
 * @param onClearClick A callback that will be called when the clear button is clicked.
 * @param modifier The modifier to be applied to the clear button.
 */
@Composable
private fun ClearButton(
  modifier: Modifier = Modifier,
  onClearClick: () -> Unit,
) {
  val containerColor = MaterialTheme.colorScheme.surfaceVariant
  OutlinedButton(
    onClick = onClearClick,
    modifier = modifier
      .testTag(TestTags.Components.Button.CLEAR_FILTERS)
      .size(MaterialTheme.dimensions.keyline_40),
    border = null,
    shape = CircleShape,
    contentPadding = PaddingValues(0.dp),
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = containerColor,
      contentColor = contentColorFor(containerColor),
    ),
  ) {
    Icon(
      imageVector = Icons.Default.Clear,
      contentDescription = stringResource(id = R.string.clear_filters_button_content_description),
    )
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
