package com.divinelink.feature.credits.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.format
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreditsTabs(
  tabs: List<CreditsTab>,
  selectedIndex: Int,
  onClick: (Int) -> Unit,
) {
  SecondaryTabRow(selectedTabIndex = selectedIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(
        modifier = Modifier.testTag(TestTags.Credits.TAB_BAR.format(tab)),
        text = {
          Text(
            text = stringResource(tab.titleRes, tab.size),
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
