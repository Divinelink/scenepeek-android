package com.divinelink.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.toStub
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_clear_history
import com.divinelink.core.ui.resources.core_ui_remove_item_from_history_content_desc
import com.divinelink.feature.search.resources.Res
import com.divinelink.feature.search.resources.recent_searches
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchHistoryContent(
  state: LazyListState,
  onNavigate: (Navigation) -> Unit,
  action: (SearchAction) -> Unit,
  history: List<MediaItem>,
) {
  ScenePeekLazyColumn(
    state = state,
  ) {
    item {
      Row(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = stringResource(Res.string.recent_searches),
          style = MaterialTheme.typography.titleSmall,
        )

        Spacer(Modifier.weight(1f))

        TextButton(
          onClick = { action(SearchAction.ClearHistory) },
        ) {
          Text(text = stringResource(UiString.core_ui_clear_history))
        }
      }
    }
    items(
      items = history,
      key = { item -> "search:history:${item.uniqueIdentifier}" },
    ) { item ->
      Row(
        modifier = Modifier
          .animateItem()
          .clickable { item.toRoute()?.let { route -> onNavigate(route) } }
          .padding(horizontal = MaterialTheme.dimensions.keyline_16)
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
          imageVector = Icons.Default.History,
          contentDescription = null,
        )
        Column(
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
          onClick = { action(SearchAction.RemoveFromHistory(item.toStub())) },
        ) {
          Icon(
            modifier = Modifier.size(MaterialTheme.dimensions.keyline_20),
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(
              UiString.core_ui_remove_item_from_history_content_desc,
              item.name,
            ),
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
