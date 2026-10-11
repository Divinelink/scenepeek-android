package com.divinelink.feature.home.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.navigation.route.Navigation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WatchHistoryContent(
  viewModel: WatchHistoryViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  ScenePeekLazyColumn(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    contentPadding = PaddingValues(
      vertical = MaterialTheme.dimensions.keyline_8,
    ),
  ) {
//    if (state.available.isEmpty() && state.upcoming.isEmpty() && state.paused.isEmpty()) {
//      item {
//        NextUpEmptyContent(
//          modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
//        )
//      }
//    }

    items(
      items = state.history,
      key = { episode -> episode.episode.id },
    ) { episode ->
      WatchedEpisodeItem(
        boxModifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_8),
        episode = episode,
        onNavigate = onNavigate,
      )
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
