package com.divinelink.core.ui.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_tab_episode
import com.divinelink.core.model.tab.EpisodeTab
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.extension.format
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeTabs(
  modifier: Modifier = Modifier,
  tabs: List<EpisodeTab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  Surface(modifier = Modifier.fillMaxWidth()) {
    PrimaryScrollableTabRow(
      edgePadding = MaterialTheme.dimensions.keyline_0,
      modifier = modifier.fillMaxWidth(),
      selectedTabIndex = (selectedIndex).coerceIn(0, tabs.size),
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.onSurface,
      minTabWidth = MaterialTheme.dimensions.keyline_72,
    ) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          modifier = Modifier.testTag(TestTags.Tabs.TAB_ITEM.format(tab.number)),
          text = {
            Text(
              text = stringResource(
                Res.string.core_model_tab_episode,
                tab.number.toString().padStart(2, '0'),
              ),
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
              color = if (index == selectedIndex) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant
              },
            )
          },
          selected = index == selectedIndex,
          onClick = { onClick(index) },
        )
      }
    }
  }
}

@Composable
@Previews
fun EpisodeTabsPreview() {
  PreviewLocalProvider {
    EpisodeTabs(
      tabs = (1..10).map { EpisodeTab(it) },
      selectedIndex = 0,
      onClick = {},
    )
  }
}
