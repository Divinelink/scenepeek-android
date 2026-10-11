package com.divinelink.feature.home.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.DateFormatStyle
import com.divinelink.core.commons.formatLocalized
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.resources.empty_history_subtitle
import com.divinelink.core.ui.resources.empty_history_title
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchHistoryContent(
  viewModel: WatchHistoryViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  val scope = rememberCoroutineScope()
  val scrollState = rememberLazyListState()

  scrollState.EndlessScrollHandler {
    viewModel.onAction(WatchHistoryAction.LoadMore)
  }

  if (state.history.flatMap { it.value }.isEmpty()) {
    BlankSlate(
      modifier = Modifier.verticalScroll(rememberScrollState()),
      uiState = BlankSlateState.Custom(
        title = UIText.ResourceText(UiString.empty_history_title),
        description = UIText.ResourceText(UiString.empty_history_subtitle),
      ),
    )
  } else {
    Box(Modifier.fillMaxSize()) {
      ScenePeekLazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        contentPadding = PaddingValues(
          vertical = MaterialTheme.dimensions.keyline_8,
        ),
      ) {
        state.history.forEach { item ->
          stickyHeader {
            Text(
              modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.keyline_8),
              text = item.key.formatLocalized(DateFormatStyle.FULL),
              style = MaterialTheme.typography.titleMedium,
            )
          }

          items(
            items = item.value,
            key = { episode -> episode.episode.id },
          ) { episode ->
            WatchedEpisodeItem(
              boxModifier = Modifier
                .padding(horizontal = MaterialTheme.dimensions.keyline_8),
              episode = episode,
              onNavigate = onNavigate,
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
        }
      }

      ScrollToTopButton(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = scrollState.canScrollToTop(),
        onClick = { scope.launch { scrollState.animateScrollToItem(0) } },
      )
    }
  }
}
