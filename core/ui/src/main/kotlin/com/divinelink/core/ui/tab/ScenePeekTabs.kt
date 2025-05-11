package com.divinelink.core.ui.tab

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
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
fun ScenePeekTabs(
  tabs: List<Tab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  PrimaryTabRow(selectedIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(
        modifier = Modifier.testTag(TestTags.Person.TAB_BAR.format(tab.value)),
        text = { Text(stringResource(tab.titleRes)) },
        selected = index == selectedIndex,
        onClick = { onClick(index) },
      )
    }
  }
}
