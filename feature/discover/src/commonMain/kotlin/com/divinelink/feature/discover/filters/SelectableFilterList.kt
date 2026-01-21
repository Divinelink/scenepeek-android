package com.divinelink.feature.discover.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.discover.ui.SearchField
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : Any> SelectableFilterList(
  titleRes: StringResource,
  items: List<T>,
  key: (T) -> Any,
  isSelected: (T) -> Boolean,
  onItemClick: (T) -> Unit,
  selected: T?,
  itemName: @Composable (T) -> String,
  query: String?,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val state = rememberLazyListState()

  LaunchedEffect(Unit) {
    val selectedIndex = items.indexOf(selected)
    if (selectedIndex != -1) {
      state.animateScrollToItem(
        index = selectedIndex,
        scrollOffset = -800,
      )
    }
  }

  LazyColumn(
    state = state,
    modifier = modifier
      .fillMaxHeight()
      .animateContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(MaterialTheme.dimensions.keyline_16),
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleMedium,
      )
    }

    stickyHeader {
      SearchField(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
          )
          .padding(MaterialTheme.dimensions.keyline_16),
        value = query,
        onValueChange = onValueChange,
      )

      HorizontalDivider()
    }

    items(
      items = items,
      key = key,
    ) { item ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .animateItem()
          .clickable { onItemClick(item) }
          .padding(
            horizontal = MaterialTheme.dimensions.keyline_20,
            vertical = MaterialTheme.dimensions.keyline_8,
          ),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          modifier = Modifier
            .weight(1f)
            .padding(MaterialTheme.dimensions.keyline_8),
          text = itemName(item),
          style = MaterialTheme.typography.bodyMedium,
        )

        AnimatedVisibility(isSelected(item)) {
          Icon(
            imageVector = Icons.Default.Done,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
          )
        }
      }

      HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
      )
    }
  }
}
