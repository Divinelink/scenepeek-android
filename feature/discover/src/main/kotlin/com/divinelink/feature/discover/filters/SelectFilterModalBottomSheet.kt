package com.divinelink.feature.discover.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.feature.discover.FilterModal
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectFilterModalBottomSheet(
  type: FilterModal,
  mediaType: MediaType,
  viewModel: SelectFilterViewModel = koinViewModel(
    key = "SelectGenreModalBottomSheet-${mediaType.value}-$type",
  ) { parametersOf(mediaType, type) },
  onDismissRequest: () -> Unit,
) {
  val density = LocalDensity.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    when (type) {
      FilterModal.Genre -> SelectGenresContent(
        uiState = uiState,
        viewModel = viewModel,
        density = density,
        onDismissRequest = onDismissRequest,
      )
      FilterModal.Language -> SelectableFilterList(
        titleRes = UiString.core_ui_language,
        items = uiState.languages,
        key = { it.code },
        isSelected = { it == uiState.selectedLanguage },
        onItemClick = {
          viewModel.onAction(SelectFilterAction.SelectLanguage(it))
          onDismissRequest()
        },
        itemName = { stringResource(it.nameRes) },
      )
      FilterModal.Country -> SelectableFilterList(
        titleRes = UiString.core_ui_country,
        items = uiState.countries,
        key = { it.code },
        isSelected = { it == uiState.selectedCountry },
        onItemClick = {
          viewModel.onAction(SelectFilterAction.SelectCountry(it))
          onDismissRequest()
        },
        itemName = { stringResource(it.nameRes) + "  ${it.flag}" },
      )
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}

@Composable
private fun SelectGenresContent(
  uiState: SelectFilterUiState,
  viewModel: SelectFilterViewModel,
  density: Density,
  onDismissRequest: () -> Unit,
) {
  Box {
    var actionsSize by remember { mutableStateOf(0.dp) }

    SelectableFilterList(
      modifier = Modifier.padding(
        bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8),
      ),
      titleRes = UiString.core_ui_genres,
      items = uiState.genres,
      key = { it.id },
      isSelected = { it in uiState.selectedGenres },
      onItemClick = { viewModel.onAction(SelectFilterAction.SelectGenre(it)) },
      itemName = { it.name },
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
        enabled = uiState.selectedGenres.isNotEmpty(),
        modifier = Modifier.weight(1f),
        onClick = {
          viewModel.onAction(SelectFilterAction.ClearGenres)
          onDismissRequest()
        },
      ) {
        Text(text = stringResource(UiString.core_ui_clear_all))
      }

      Button(
        enabled = uiState.selectedGenres.isNotEmpty(),
        modifier = Modifier.weight(1f),
        onClick = onDismissRequest,
      ) {
        Text(
          text = if (uiState.selectedGenres.isEmpty()) {
            stringResource(UiString.core_ui_apply_filters)
          } else {
            pluralStringResource(
              UiPlurals.core_ui_apply_filters,
              uiState.selectedGenres.size,
              uiState.selectedGenres.size,
            )
          },
        )
      }
    }
  }
}

@Composable
fun <T : Any> SelectableFilterList(
  titleRes: Int,
  items: List<T>,
  key: (T) -> Any,
  isSelected: (T) -> Boolean,
  onItemClick: (T) -> Unit,
  itemName: @Composable (T) -> String,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleMedium,
      )
      HorizontalDivider()
    }

    items(
      items = items,
      key = key,
    ) { item ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onItemClick(item) }
          .padding(
            horizontal = MaterialTheme.dimensions.keyline_20,
            vertical = MaterialTheme.dimensions.keyline_8,
          ),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          modifier = Modifier
            .weight(1f)
            .padding(MaterialTheme.dimensions.keyline_8),
          text = itemName(item),
          style = MaterialTheme.typography.bodyMedium,
        )

        AnimatedVisibility(isSelected(item)) {
          Icon(
            imageVector = Icons.Default.Done,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
          )
        }
      }

      HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
      )
    }
  }
}
