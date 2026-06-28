package com.divinelink.feature.awards.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.awards.category.AwardCategoryAction
import com.divinelink.feature.awards.category.AwardCategoryUiState
import com.divinelink.feature.awards.category.ui.provider.AwardCategoryUiStateParameterProvider

@Composable
fun AwardCategoriesListContent(
  uiState: AwardCategoryUiState,
  action: (AwardCategoryAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val state = rememberLazyGridState()

  LazyVerticalGrid(
    state = state,
    modifier = Modifier,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_16,
    ),
    columns = GridCells.Adaptive(mediaCardSize()),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    uiState.awards.entries.forEach { award ->
      stickyHeader(key = award.key) {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .offset(x = -MaterialTheme.dimensions.keyline_8)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimensions.keyline_8),
          text = award.key,
          style = MaterialTheme.typography.titleMedium,
        )
      }

      items(
        items = award.value,
        key = { award.key + it.item.media.mediaId },
      ) { item ->
        NomineeMediaItem(
          modifier = Modifier
            .onFirstVisible(
              minDurationMs = 100L,
              minFractionVisible = 0.1f,
            ) {
              action.invoke(AwardCategoryAction.FetchMediaItem(item))
            }
            .animateItem(),
          item = item,
          onNavigate = onNavigate,
          onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
          onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
        )
      }
    }
  }
}

@Composable
@Previews
fun AwardCategoryContentsPreview(
  @PreviewParameter(AwardCategoryUiStateParameterProvider::class) state: AwardCategoryUiState,
) {
  PreviewLocalProvider {
    AwardCategoryContent(
      uiState = state,
      action = {},
      onNavigate = {},
    )
  }
}
