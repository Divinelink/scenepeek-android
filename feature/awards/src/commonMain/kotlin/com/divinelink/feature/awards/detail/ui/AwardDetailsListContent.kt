package com.divinelink.feature.awards.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.awards.Res
import com.divinelink.feature.awards.categories
import com.divinelink.feature.awards.detail.AwardDetailsAction
import com.divinelink.feature.awards.detail.AwardDetailsUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun AwardDetailsListContent(
  uiState: AwardDetailsUiState,
  action: (AwardDetailsAction) -> Unit,
) {
  val state = rememberLazyGridState()

  LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = MaterialTheme.dimensions.posterSizeSmall),
    state = state,
    modifier = Modifier,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_8,
      bottom = LocalBottomNavigationPadding.current,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    if (uiState.categories.isNotEmpty()) {
      item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
          text = stringResource(Res.string.categories),
          style = MaterialTheme.typography.titleMedium,
        )
      }
      item(span = { GridItemSpan(maxLineSpan) }) {
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          uiState.categories.forEach { category ->
            Card(
              onClick = { action(AwardDetailsAction.OnCategoryClick(category)) },
            ) {
              Text(
                modifier = Modifier.padding(
                  horizontal = MaterialTheme.dimensions.keyline_16,
                  vertical = MaterialTheme.dimensions.keyline_12,
                ),
                text = category.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
              )
            }
          }
        }
      }
    }
  }
}
