package com.divinelink.feature.requests.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.filter.Filter
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.resources.core_ui_filter
import com.divinelink.core.ui.resources.core_ui_filter_button_content_desc
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T : Filter> FilterButton(
  modifier: Modifier = Modifier,
  filter: T?,
  filters: List<T>,
  onApplyFilter: (T) -> Unit,
) {
  var showModal by rememberSaveable { mutableStateOf(false) }

  if (showModal) {
    FilterModal(
      onDismissRequest = { showModal = false },
      onApplyFilter = {
        onApplyFilter(it)
        showModal = false
      },
      filters = filters,
      appliedFilter = filter,
    )
  }

  AnimatedContent(
    modifier = Modifier.testTag(TestTags.Components.FILTER_BUTTON),
    targetState = filter == null,
  ) { isNull ->
    if (isNull) {
      IconButton(
        onClick = { showModal = true },
      ) {
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
          .clickable(
            onClick = { showModal = true },
          )
          .padding(
            vertical = MaterialTheme.dimensions.keyline_8,
            horizontal = MaterialTheme.dimensions.keyline_16,
          ),
      ) {
        if (filter != null) {
          Icon(
            imageVector = Icons.Outlined.FilterList,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
          )
        }

        Text(
          text = filter?.name?.getString() ?: "",
          color = MaterialTheme.colorScheme.primary,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Filter> FilterModal(
  filters: List<T>,
  appliedFilter: T?,
  onDismissRequest: () -> Unit,
  onApplyFilter: (T) -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    modifier = Modifier.testTag(TestTags.Modal.ACTION_MENU),
    shape = MaterialTheme.shapes.extraLarge,
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    content = {
      CreditsFilterBottomSheetContent(
        filters = filters,
        appliedFilter = appliedFilter,
        onClick = onApplyFilter,
      )
    },
  )
}

@Composable
private fun <T : Filter> CreditsFilterBottomSheetContent(
  filters: List<T>,
  appliedFilter: T?,
  onClick: (T) -> Unit,
) {
  LazyColumn {
    item {
      Text(
        text = stringResource(UiString.core_ui_filter),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
      )
    }

    item {
      Spacer(Modifier.padding(MaterialTheme.dimensions.keyline_8))
      HorizontalDivider()
      Spacer(Modifier.padding(MaterialTheme.dimensions.keyline_8))
    }

    items(filters) {
      FilterItem(
        filter = it,
        isApplied = appliedFilter == it,
        onClick = onClick,
      )
    }
  }
}

@Composable
private fun <T : Filter> FilterItem(
  filter: T,
  isApplied: Boolean,
  onClick: (T) -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(filter) },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = filter.name.getString(),
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(
          horizontal = MaterialTheme.dimensions.keyline_16,
          vertical = MaterialTheme.dimensions.keyline_12,
        ),
    )
    if (isApplied) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(end = MaterialTheme.dimensions.keyline_16),
      )
    }
  }
}
