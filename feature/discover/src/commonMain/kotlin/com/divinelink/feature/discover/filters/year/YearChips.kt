package com.divinelink.feature.discover.filters.year

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.discover.YearType
import org.jetbrains.compose.resources.stringResource

@Composable
fun YearChips(
  chips: List<YearType>,
  selected: YearType,
  onClick: (YearType) -> Unit,
) {
  FlowRow(
    modifier = Modifier
      .animateContentSize()
      .padding(horizontal = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    chips.forEach { tab ->
      FilterChip(
        selected = tab == selected,
        label = {
          Text(
            text = stringResource(tab.label),
            style = MaterialTheme.typography.titleSmall,
          )
        },
        onClick = { onClick(tab) },
      )
    }
  }
}
