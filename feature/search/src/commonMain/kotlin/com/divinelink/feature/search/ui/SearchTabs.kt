package com.divinelink.feature.search.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.tab.SearchTab
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchTabs(
  tabs: List<SearchTab>,
  selected: SearchTab,
  onClick: (SearchTab) -> Unit,
) {
  LazyRow(
    modifier = Modifier
      .animateContentSize()
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalAlignment = Alignment.CenterVertically,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_16,
    ),
  ) {
    items(tabs) { tab ->
      FilterChip(
        selected = tab == selected,
        label = {
          Text(
            text = stringResource(tab.titleRes),
            style = MaterialTheme.typography.titleSmall,
          )
        },
        onClick = { onClick(tab) },
      )
    }
  }
}
