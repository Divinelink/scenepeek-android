package com.divinelink.feature.discover.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.resources.core_ui_apply_filters
import com.divinelink.core.ui.resources.core_ui_clear_all
import com.divinelink.core.ui.resources.core_ui_genres
import com.divinelink.feature.discover.FilterType
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectGenresContent(
  uiState: SelectFilterUiState,
  action: (SelectFilterAction) -> Unit,
  density: Density,
  onDismissRequest: () -> Unit,
) {
  when {
    uiState.loading -> LoadingContent(modifier = Modifier.fillMaxSize())
    uiState.error != null -> BlankSlate(
      uiState = uiState.error,
      onRetry = { action(SelectFilterAction.Retry) },
    )
    else -> Box {
      var actionsSize by remember { mutableStateOf(0.dp) }
      val filterType = uiState.filterType as FilterType.Searchable.Genres

      SelectableFilterList(
        modifier = Modifier.padding(
          bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8),
        ),
        titleRes = UiString.core_ui_genres,
        items = filterType.visibleOptions,
        key = { it.id },
        isSelected = { it in uiState.filterType.selectedOptions },
        onItemClick = { action(SelectFilterAction.SelectGenre(it)) },
        selected = uiState.filterType.selectedOptions.firstOrNull(),
        itemName = { it.name },
        onValueChange = { action(SelectFilterAction.SearchFilters(it)) },
        query = filterType.query,
      )

      Row(
        modifier = Modifier
          .onSizeChanged {
            with(density) {
              actionsSize = it.height.toDp()
            }
          }
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .align(Alignment.BottomCenter)
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        ElevatedButton(
          enabled = filterType.selectedOptions.isNotEmpty(),
          modifier = Modifier.weight(1f),
          onClick = {
            action(SelectFilterAction.ClearGenres)
            onDismissRequest()
          },
        ) {
          Text(text = stringResource(UiString.core_ui_clear_all))
        }

        Button(
          enabled = filterType.selectedOptions.isNotEmpty(),
          modifier = Modifier.weight(1f),
          onClick = onDismissRequest,
        ) {
          Text(
            text = if (filterType.selectedOptions.isEmpty()) {
              stringResource(UiString.core_ui_apply_filters)
            } else {
              pluralStringResource(
                UiPlurals.core_ui_apply_filters,
                filterType.selectedOptions.size,
                filterType.selectedOptions.size,
              )
            },
          )
        }
      }
    }
  }
}
