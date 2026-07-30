package com.divinelink.feature.discover.filters.keyword

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.resources.core_ui_clear_all
import com.divinelink.core.ui.resources.core_ui_keywords
import com.divinelink.core.ui.resources.core_ui_search_keywords_description
import com.divinelink.feature.discover.FilterType
import com.divinelink.feature.discover.filters.SelectFilterAction
import com.divinelink.feature.discover.ui.SearchField
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource

@Composable
fun KeywordsFiltersContent(
  keywords: FilterType.Keywords,
  action: (SelectFilterAction) -> Unit,
  onDismissRequest: () -> Unit,
) {
  val state = rememberLazyListState()
  val density = LocalDensity.current
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(state) {
    snapshotFlow { state.isScrollInProgress }
      .distinctUntilChanged()
      .collect { isScrolling ->
        if (isScrolling) {
          keyboardController?.hide()
          focusManager.clearFocus()
        }
      }
  }

  Box {
    var actionsSize by remember { mutableStateOf(0.dp) }

    LazyColumn(
      modifier = Modifier
        .padding(bottom = actionsSize.plus(MaterialTheme.dimensions.keyline_8))
        .fillMaxSize(),
      contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      state = state,
    ) {
      item {
        Text(
          textAlign = TextAlign.Start,
          text = stringResource(UiString.core_ui_keywords),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      stickyHeader {
        SearchField(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              color = MaterialTheme.colorScheme.surfaceContainerLow,
            )
            .padding(top = MaterialTheme.dimensions.keyline_8)
            .padding(vertical = MaterialTheme.dimensions.keyline_16),
          value = keywords.query,
          onValueChange = { action(SelectFilterAction.SearchKeywords(it)) },
        )

        Box {
          AnimatedContent(
            targetState = keywords.loading,
          ) { loading ->
            if (loading) {
              AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = keywords.loading,
              ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
              }
            } else {
              HorizontalDivider(modifier = Modifier.align(Alignment.Center))
            }
          }
        }
      }

      item {
        if (keywords.visibleOptions().isEmpty()) {
          BlankSlate(
            uiState = BlankSlateState.Custom(
              title = UIText.StringText(""),
              description = UIText.ResourceText(UiString.core_ui_search_keywords_description),
            ),
          )
        }
      }

      item {
        FlowRow(
          modifier = Modifier.matchParentSize(),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          keywords.visibleOptions().forEach { keyword ->
            key(keyword.id) {
              InputChip(
                modifier = Modifier.animateItem(),
                selected = keyword in keywords.selectedOptions,
                onClick = { action(SelectFilterAction.SelectKeyword(keyword)) },
                label = { Text(keyword.name) },
              )
            }
          }
        }
      }
    }

    Row(
      modifier = Modifier
        .onSizeChanged { with(density) { actionsSize = it.height.toDp() } }
        .padding(horizontal = MaterialTheme.dimensions.keyline_16)
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ElevatedButton(
        enabled = keywords.visibleOptions().isNotEmpty(),
        modifier = Modifier.weight(1f),
        onClick = {
          action(SelectFilterAction.ClearKeywords)
          onDismissRequest()
        },
      ) {
        Text(text = stringResource(UiString.core_ui_clear_all))
      }
    }
  }
}
