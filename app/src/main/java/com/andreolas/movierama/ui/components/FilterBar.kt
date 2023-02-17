@file:Suppress("MagicNumber")
package com.andreolas.movierama.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.theme.AppTheme

/**
 * A composable that will display a list of filters.
 *
 * @param filters The list of filters to display.
 * @param onFilterClick A callback that will be called when a homeFilters is clicked.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterBar(
    modifier: Modifier = Modifier,
    filters: List<Filter>,
    onFilterClick: (Filter) -> Unit,
    onClearClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val sortedFilters = filters.sortedBy { !it.isSelected }

        if (sortedFilters.any { it.isSelected }) {
            item {
                ClearButton(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 600,
                        ),
                    ),
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
                modifier = Modifier.animateItemPlacement(
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
    // This button should have a rounded corner.
    val containerColor = MaterialTheme.colorScheme.surface
    IconButton(
        modifier = modifier
            .clip(RoundedCornerShape(100)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            contentColor = contentColorFor(containerColor),
        ),

        onClick = onClearClick,
    ) {
        Icon(
            modifier = modifier.clip(RoundedCornerShape(100)),
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
 * A data class that will hold the homeFilters name and if it is selected or not.
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
