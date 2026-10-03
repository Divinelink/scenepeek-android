package com.divinelink.core.ui.button.switchview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.rememberListSortOption
import com.divinelink.core.ui.composition.rememberSortBy
import com.divinelink.core.ui.resources.core_ui_change_sort_direction
import com.divinelink.core.ui.resources.core_ui_default
import org.jetbrains.compose.resources.stringResource

@Composable
fun SortingButton(
  section: SortableSection,
  listId: Int? = null,
  enabled: Boolean,
  onSwitchSortDirection: (SortableSection) -> Unit,
  onSwitchSortBy: (SortableSection, SortBy) -> Unit,
  onClearListSortOption: (listId: Int) -> Unit,
) {
  val sortBy = if (listId == null) {
    rememberSortBy(section)
  } else {
    rememberListSortOption(listId)
  }

  var showDropdownSortByOptions by remember { mutableStateOf(false) }
  var descending by remember(sortBy?.direction) {
    mutableStateOf(sortBy?.direction == SortDirection.DESC)
  }

  val rotationState by remember(sortBy?.direction) {
    derivedStateOf { if (descending) 0f else 180f }
  }
  val rotation by animateFloatAsState(targetValue = rotationState)

  val options = section.sortContext.availableOptions

  if (showDropdownSortByOptions) {
    DropdownMenu(
      modifier = Modifier
        .widthIn(min = 180.dp)
        .testTag(TestTags.Menu.DROPDOWN_MENU),
      expanded = showDropdownSortByOptions,
      onDismissRequest = { showDropdownSortByOptions = false },
    ) {
      options.forEach { option ->
        SortDropdownMenuItem(
          sortBy = option,
          onClick = {
            showDropdownSortByOptions = false
            onSwitchSortBy(section, option)
          },
        )
      }

      if (listId != null) {
        HorizontalDivider(
          modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.keyline_4),
        )

        DropdownMenuItem(
          modifier = Modifier,
          text = {
            Text(text = stringResource(UiString.core_ui_default))
          },
          onClick = {
            showDropdownSortByOptions = false
            onClearListSortOption(listId)
          },
        )
      }
    }
  }

  if (sortBy == null) {
    IconButton(
      onClick = { showDropdownSortByOptions = true },
      enabled = enabled,
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.Sort,
        contentDescription = null,
        tint = if (enabled) {
          MaterialTheme.colorScheme.primary
        } else {
          MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        },
      )
    }
  } else {
    TextButton(
      enabled = options.isNotEmpty() && options.size != 1 && enabled,
      onClick = { showDropdownSortByOptions = true },
      colors = ButtonDefaults.textButtonColors().copy(
        disabledContentColor = ButtonDefaults.textButtonColors().contentColor.copy(
          alpha = 0.7f,
        ),
      ),
    ) {
      Text(stringResource(sortBy.sortBy.label))
    }

    IconButton(
      enabled = enabled,
      modifier = Modifier
        .offset(x = -MaterialTheme.dimensions.keyline_12)
        .clip(shape = MaterialTheme.shapes.large)
        .testTag(TestTags.Components.Button.SWITCH_VIEW),
      onClick = { onSwitchSortDirection(section) },
    ) {
      Icon(
        modifier = Modifier.rotate(rotation),
        imageVector = Icons.Outlined.ArrowDownward,
        contentDescription = stringResource(UiString.core_ui_change_sort_direction),
        tint = if (enabled) {
          MaterialTheme.colorScheme.primary
        } else {
          MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        },
      )
    }
  }
}

@Composable
private fun SortDropdownMenuItem(
  sortBy: SortBy,
  onClick: (SortBy) -> Unit,
) {
  DropdownMenuItem(
    modifier = Modifier,
    text = { Text(text = stringResource(sortBy.label)) },
    onClick = { onClick(sortBy) },
  )
}
