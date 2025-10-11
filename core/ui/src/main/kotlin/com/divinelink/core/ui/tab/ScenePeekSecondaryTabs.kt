package com.divinelink.core.ui.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePeekSecondaryTabs(
  modifier: Modifier = Modifier,
  tabs: List<Tab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  Surface(modifier = Modifier.fillMaxWidth()) {
    SecondaryTabRow(
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
