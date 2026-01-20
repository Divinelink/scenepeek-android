package com.divinelink.core.ui.button.switchview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.rememberSortBy
import com.divinelink.core.ui.resources.core_ui_change_sort_direction
import org.jetbrains.compose.resources.stringResource

@Composable
fun SortingButton(
  section: ViewableSection,
  onSwitchSortDirection: (ViewableSection) -> Unit,
  onSwitchSortBy: (ViewableSection, SortBy) -> Unit,
) {
  val sortBy = rememberSortBy(section)

  var showDropdownSortByOptions by remember { mutableStateOf(false) }
  var descending by rememberSaveable(sortBy) {
    mutableStateOf(sortBy.direction == SortDirection.DESC)
  }
  val rotationState by remember(sortBy) { derivedStateOf { if (descending) 0f else 180f } }
  val rotation by animateFloatAsState(targetValue = rotationState)

  if (showDropdownSortByOptions) {
    DropdownMenu(
      modifier = Modifier
        .widthIn(min = 180.dp)
        .testTag(TestTags.Menu.DROPDOWN_MENU),
      expanded = showDropdownSortByOptions,
      onDismissRequest = { showDropdownSortByOptions = false },
    ) {
      val options = when (section) {
        ViewableSection.DISCOVER_SHOWS -> SortBy.discoverShowEntries
        ViewableSection.DISCOVER_MOVIES -> SortBy.discoverMovieEntries
        else -> emptyList()
      }

      options.forEach { option ->
        SortDropdownMenuItem(
          sortBy = option,
          onClick = {
            showDropdownSortByOptions = false
            onSwitchSortBy(section, option)
          },
        )
      }
    }
  }

  TextButton(
    onClick = { showDropdownSortByOptions = true },
  ) {
    Text(stringResource(sortBy.sortBy.label))
  }

  IconButton(
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
      tint = MaterialTheme.colorScheme.primary,
    )
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
