package com.divinelink.feature.discover.filters.year

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectYearField(
  modifier: Modifier = Modifier,
  title: String? = null,
  year: Int,
  onClick: () -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_2),
  ) {
    title?.let {
      Text(
        modifier = Modifier
          .padding(start = MaterialTheme.dimensions.keyline_10),
        text = title,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Row(
      modifier = Modifier
        .border(
          width = MaterialTheme.dimensions.keyline_2,
          color = MaterialTheme.colorScheme.outlineVariant,
          shape = MaterialTheme.shapes.small,
        )
        .clip(MaterialTheme.shapes.small)
        .clickable { onClick() }
        .padding(MaterialTheme.dimensions.keyline_16)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        modifier = Modifier
          .weight(1f),
        text = year.toString(),
      )

      Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = null,
      )
    }
  }
}
