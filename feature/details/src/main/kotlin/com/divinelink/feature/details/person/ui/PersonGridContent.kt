package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.CreditMediaItem
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.feature.details.R
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import timber.log.Timber

@Composable
internal fun PersonGridContent(
  modifier: Modifier = Modifier,
  itemModifier: Modifier = Modifier,
  grid: GridCells,
  credits: GroupedPersonCredits,
  filters: List<CreditFilter>,
  isGrid: Boolean,
  onMediaClick: (MediaItem) -> Unit,
  setCurrentDepartment: (String) -> Unit,
) {
  val lazyGridState = rememberLazyGridState()

  val currentDepartments = credits.keys.toList()
  val headerPositions = remember(currentDepartments) {
    val positions = mutableListOf<Int>()
    var currentIndex = 0
    currentDepartments.forEach { department ->
      positions.add(currentIndex)
      // 1 for the header + the size of the department
      currentIndex += 1 + (credits[department]?.size ?: 0)
    }
    positions
  }

  val currentDepartment by remember {
    derivedStateOf {
      val firstVisibleIndex = lazyGridState.firstVisibleItemIndex

      // Find the last header position <= first visible index
      headerPositions
        .lastOrNull { it <= firstVisibleIndex }
        ?.let { headerIndex ->
          currentDepartments.getOrNull(headerPositions.indexOf(headerIndex))
        } ?: ""
    }
  }

  LaunchedEffect(currentDepartment) {
    Timber.d("Current visible department: $currentDepartment")
    setCurrentDepartment(currentDepartment)
  }

  LazyVerticalGrid(
    columns = grid,
    state = lazyGridState,
    modifier = modifier,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_8,
      bottom = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    credits.forEach { credit ->
      if (filters.isEmpty()) {
        item(span = { GridItemSpan(maxLineSpan) }) {
          Text(
            modifier = Modifier
              .background(MaterialTheme.colorScheme.surface)
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_8,
                vertical = MaterialTheme.dimensions.keyline_8,
              ),
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(
              R.string.feature_details_person_credits_department_size,
              credit.key,
              credit.value.size,
            ),
          )
        }
      }

      items(
        items = credit.value,
        key = { "${it.mediaItem.id} ${it.role.title}" },
      ) { item ->
        if (isGrid) {
          MediaItem(
            modifier = itemModifier,
            media = item.mediaItem,
            subtitle = item.role.title,
            fullDate = false,
            onMediaItemClick = onMediaClick,
          )
        } else {
          CreditMediaItem(
            modifier = itemModifier,
            mediaItem = item.mediaItem,
            subtitle = item.role.title,
            onClick = onMediaClick,
          )
        }
      }
    }
  }
}
