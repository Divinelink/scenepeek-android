package com.divinelink.feature.home.customize

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.ui.draganddrop.DraggableItem
import com.divinelink.core.ui.draganddrop.dragContainer
import com.divinelink.core.ui.draganddrop.rememberDragDropState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReorderableHomeSections(
  modifier: Modifier = Modifier,
  uiState: CustomizeDashboardUiState,
  onShowOrHide: (MediaListSection.Home) -> Unit,
  onReorderSections: (List<HomeSectionInfo>) -> Unit,
) {
  val sections = remember { mutableStateListOf<HomeSectionInfo>() }

  LaunchedEffect(uiState.sections) {
    sections.clear()
    sections.addAll(uiState.sections)
  }

  val listState = rememberLazyListState()
  val haptics = LocalHapticFeedback.current

  val dragDropState = rememberDragDropState(
    lazyListState = listState,
    onMove = { from, to ->
      sections.add(to.index, sections.removeAt(from.index))
    },
    onDragEnd = {
      onReorderSections(sections)
      haptics.performHapticFeedback(HapticFeedbackType.LongPress)
    },
  )

  LazyColumn(
    state = listState,
    modifier = modifier
      .fillMaxSize()
      .dragContainer(dragDropState),
    contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    itemsIndexed(
      items = sections,
      key = { _, section -> section.key },
    ) { index, task ->
      DraggableItem(dragDropState, index) { isDragging ->
        SectionRow(
          title = stringResource(task.title),
          isDragging = isDragging,
          isHidden = task in uiState.hiddenSections,
          onShowOrHide = { onShowOrHide(task.section) },
        )
      }
    }
  }
}

@Composable
private fun SectionRow(
  title: String,
  isDragging: Boolean,
  isHidden: Boolean,
  onShowOrHide: () -> Unit,
) {
  val elevation by animateDpAsState(
    targetValue = if (isDragging) {
      MaterialTheme.dimensions.keyline_8
    } else {
      MaterialTheme.dimensions.keyline_0
    },
  )

  val tintColor by animateColorAsState(
    targetValue = if (isHidden) {
      MaterialTheme.colorScheme.onSurfaceVariant
    } else {
      MaterialTheme.colorScheme.onPrimaryContainer
    },
    label = "Container color animation",
  )

  val containerColor by animateColorAsState(
    targetValue = if (isHidden) {
      MaterialTheme.colorScheme.surfaceContainer
    } else {
      MaterialTheme.colorScheme.primaryContainer
    },
    label = "Container color animation",
  )

  Card(
    colors = CardDefaults.cardColors(
      containerColor = containerColor,
    ),
    modifier = Modifier
      .fillMaxWidth()
      .shadow(elevation, MaterialTheme.shapes.medium),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(
        start = MaterialTheme.dimensions.keyline_16,
        top = MaterialTheme.dimensions.keyline_12,
        bottom = MaterialTheme.dimensions.keyline_12,
        end = MaterialTheme.dimensions.keyline_8,
      ),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.weight(1f),
        color = tintColor,
      )
      IconButton(
        onClick = onShowOrHide,
      ) {
        Icon(
          imageVector = if (isHidden) {
            Icons.Default.VisibilityOff
          } else {
            Icons.Default.Visibility
          },
          contentDescription = null,
          tint = tintColor,
        )
      }
      Icon(
        imageVector = Icons.Default.DragHandle,
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier
          .size(MaterialTheme.dimensions.keyline_40)
          .padding(MaterialTheme.dimensions.keyline_8)
          .semantics { contentDescription = "Reorder $title" },
      )
    }
  }
}
