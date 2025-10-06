package com.divinelink.feature.discover.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaType
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
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    when (type) {
      FilterModal.Genre -> SelectGenreContent(
        uiState = uiState,
        onAction = viewModel::onAction,
      )
      FilterModal.Language -> SelectLanguageContent(
        uiState = uiState,
        onAction = viewModel::onAction,
      )
    }
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
  }
}

@Composable
private fun SelectGenreContent(
  uiState: SelectFilterUiState,
  onAction: (SelectFilterAction) -> Unit,
) {
  LazyColumn(
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(UiString.core_ui_genres),
        style = MaterialTheme.typography.titleMedium,
      )
      HorizontalDivider()
    }
    items(
      items = uiState.genres,
      key = { it.id },
    ) { genre ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onAction.invoke(SelectFilterAction.SelectGenre(genre)) }
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
          text = genre.name,
          style = MaterialTheme.typography.bodyMedium,
        )

        AnimatedVisibility(genre in uiState.selectedGenres) {
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

@Composable
private fun SelectLanguageContent(
  uiState: SelectFilterUiState,
  onAction: (SelectFilterAction) -> Unit,
) {
  LazyColumn(
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(UiString.core_ui_languages),
        style = MaterialTheme.typography.titleMedium,
      )
      HorizontalDivider()
    }
    items(
      items = uiState.languages,
      key = { it.code },
    ) { language ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onAction.invoke(SelectFilterAction.SelectLanguage(language)) }
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
          text = stringResource(language.nameRes),
          style = MaterialTheme.typography.bodyMedium,
        )

        AnimatedVisibility(language in uiState.selectedLanguages) {
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
