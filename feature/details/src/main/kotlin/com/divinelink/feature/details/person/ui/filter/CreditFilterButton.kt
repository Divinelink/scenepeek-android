package com.divinelink.feature.details.person.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.R as uiR

@Composable
fun CreditFilterButton(
  appliedFilters: List<CreditFilter>,
  onFilterClick: () -> Unit,
) {
  val context = LocalContext.current

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    modifier = Modifier
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.surface)
      .clickable { onFilterClick() }
      .padding(
        vertical = MaterialTheme.dimensions.keyline_8,
        horizontal = MaterialTheme.dimensions.keyline_16,
      ),
  ) {
    if (appliedFilters.isNotEmpty()) {
      Text(
        text = appliedFilters.joinToString { it.title.getString(context) },
        color = MaterialTheme.colorScheme.primary,
      )
    } else {
      Text(
        text = stringResource(uiR.string.core_ui_filter),
        color = MaterialTheme.colorScheme.primary,
      )
    }
    Icon(
      imageVector = Icons.Outlined.FilterList,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
    )
  }
}
