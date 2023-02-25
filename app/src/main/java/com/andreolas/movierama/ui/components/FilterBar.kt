@file:Suppress("MagicNumber")

package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.theme.AppTheme

/**
 * A composable that will display a list of filters.
 *
 * @param modifier The modifier to be applied to the filter bar.
 * @param filters The list of filters to display.
 * @param onFilterClick A callback that will be called when a filter is clicked.
 * @param onClearClick A callback that will be called when the clear button is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterBar(
    modifier: Modifier = Modifier,
    filters: List<Filter>,
    onFilterClick: (Filter) -> Unit,
    onClearClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyRow(
            modifier = modifier
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
                    visible = sortedFilters.any { it.isSelected }
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
                    modifier = Modifier
                        .animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 600,
                            ),
                        ),
                    filter = filter,
                    onFilterClick = {
                        onFilterClick(it)
                    },
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
            .size(40.dp),
        border = null,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColorFor(containerColor),
        )
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
        MaterialTheme.colorScheme.surfaceVariant
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FilterBarPreview() {
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FilterBarUnselectedPreview() {
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
