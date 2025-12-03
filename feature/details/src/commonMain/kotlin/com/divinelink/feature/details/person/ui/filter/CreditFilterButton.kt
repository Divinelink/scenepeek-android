package com.divinelink.feature.details.person.ui.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.resources.core_ui_filter_button_content_desc
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreditFilterButton(
  modifier: Modifier = Modifier,
  appliedFilters: List<CreditFilter>,
  onFilterClick: () -> Unit,
) {
  AnimatedContent(
    modifier = Modifier.testTag(TestTags.Components.FILTER_BUTTON),
    targetState = appliedFilters.isEmpty(),
  ) { isEmpty ->
    if (isEmpty) {
      IconButton(onFilterClick) {
        Icon(
          imageVector = Icons.Outlined.FilterList,
          contentDescription = stringResource(UiString.core_ui_filter_button_content_desc),
          tint = MaterialTheme.colorScheme.primary,
        )
      }
    } else {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        modifier = modifier
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surface)
          .clickable(onClick = onFilterClick)
          .padding(
            vertical = MaterialTheme.dimensions.keyline_8,
            horizontal = MaterialTheme.dimensions.keyline_16,
          ),
      ) {
        Text(
          text = appliedFilters.map { it.title.getString() }.joinToString(),
          color = MaterialTheme.colorScheme.primary,
        )

        if (appliedFilters.isNotEmpty()) {
          Icon(
            imageVector = Icons.Outlined.FilterList,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
          )
        }
      }
    }
  }
}
