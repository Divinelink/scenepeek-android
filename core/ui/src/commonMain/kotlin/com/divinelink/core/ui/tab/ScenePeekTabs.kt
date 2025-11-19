package com.divinelink.core.ui.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.jetbrains.compose.resources.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.tab.custom.CustomPrimaryScrollableTabRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePeekTabs(
  modifier: Modifier = Modifier,
  tabs: List<Tab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  Surface(modifier = Modifier.fillMaxWidth()) {
    CustomPrimaryScrollableTabRow(
      edgePadding = MaterialTheme.dimensions.keyline_0,
      modifier = modifier.fillMaxWidth(),
      selectedTabIndex = selectedIndex,
    ) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          modifier = Modifier.testTag(TestTags.Tabs.TAB_ITEM.format(tab.value)),
          text = {
            Text(
              text = stringResource(tab.titleRes),
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
fun ScenePeekTabsPreview() {
  AppTheme {
    Surface {
      ScenePeekTabs(
        tabs = PersonTab.entries,
        selectedIndex = 0,
        onClick = {},
      )
    }
  }
}
