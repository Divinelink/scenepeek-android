package com.divinelink.feature.details.person.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.ui.CreditMediaItem
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.feature.details.R
import com.divinelink.feature.details.person.ui.credits.EmptyContentCreditCard
import com.divinelink.feature.details.person.ui.filter.CreditFilter
import timber.log.Timber

@Composable
internal fun PersonGridContent(
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  grid: GridCells,
  credits: GroupedPersonCredits,
  filters: List<CreditFilter>,
  isGrid: Boolean,
  setCurrentDepartment: (String) -> Unit,
  mediaType: MediaType,
  name: String,
  lazyGridState: LazyGridState,
) {
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
    if (credits.isEmpty()) {
      item(span = { GridItemSpan(maxLineSpan) }) {
        EmptyContentCreditCard(
          type = mediaType,
          name = name,
        )
      }
    } else {
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
          key = { "${it.media.id} ${it.role.title}" },
        ) { item ->
          if (isGrid) {
            MediaItem(
              modifier = Modifier
                .animateItem()
                .animateContentSize(),
              media = item.media,
              subtitle = item.role.title,
              showDate = true,
              onClick = {
                onNavigate(
                  DetailsRoute(
                    id = it.id,
                    mediaType = it.mediaType,
                    isFavorite = null,
                  ),
                )
              },
              onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
            )
          } else {
            CreditMediaItem(
              modifier = Modifier
                .animateItem()
                .animateContentSize(),
              mediaItem = item.media,
              subtitle = item.role.title,
              onClick = {
                onNavigate(
                  DetailsRoute(
                    id = it.id,
                    mediaType = it.mediaType,
                    isFavorite = null,
                  ),
                )
              },
              onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
            )
          }
        }
      }
    }

    item(span = { GridItemSpan(maxLineSpan) }) {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
