package com.divinelink.feature.credits.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsTabs(
  tabs: List<CreditsTab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  Row {
    SecondaryTabRow(selectedTabIndex = selectedIndex) {
      tabs.forEachIndexed { index, tab ->
        Tab(
          modifier = Modifier.testTag(TestTags.Credits.TAB_BAR.format(tab)),
          text = { Text(stringResource(tab.titleRes, tab.size)) },
          selected = index == selectedIndex,
          onClick = { onClick(index) },
        )
      }
    }
  }
}
