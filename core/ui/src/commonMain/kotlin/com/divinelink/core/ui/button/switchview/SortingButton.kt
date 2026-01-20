package com.divinelink.core.ui.button.switchview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
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
  modifier: Modifier = Modifier,
  section: ViewableSection,
  onSwitchSortDirection: (ViewableSection) -> Unit,
  onSwitchSortBy: (ViewableSection, SortBy) -> Unit,
) {
  val sortBy = rememberSortBy(section)

  var showDropdownSortByOptions by remember { mutableStateOf(false) }
  var descending by rememberSaveable(sortBy) {
    mutableStateOf(sortBy.direction == SortDirection.DESC)
  }
  val rotationState by remember(sortBy) { derivedStateOf { if (descending) 180f else 0f } }
  val rotation by animateFloatAsState(targetValue = rotationState)

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
