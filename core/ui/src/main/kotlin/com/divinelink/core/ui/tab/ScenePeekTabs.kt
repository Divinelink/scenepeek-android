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
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.tab.PersonTab
import com.divinelink.core.model.tab.Tab
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenePeekTabs(
  tabs: List<Tab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  PrimaryScrollableTabRow(
    modifier = Modifier.fillMaxWidth(),
    selectedTabIndex = selectedIndex,
    edgePadding = MaterialTheme.dimensions.keyline_0,
  ) {
    tabs.forEachIndexed { index, tab ->
      Tab(
        modifier = Modifier.testTag(TestTags.Person.TAB_BAR.format(tab.value)),
        text = {
          Text(
            text = stringResource(tab.titleRes),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
          )
        },
        selected = index == selectedIndex,
        onClick = { onClick(index) },
      )
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
