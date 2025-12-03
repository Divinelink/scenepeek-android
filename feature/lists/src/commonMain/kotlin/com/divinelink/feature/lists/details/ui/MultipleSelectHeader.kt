package com.divinelink.feature.lists.details.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Deselect
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_close_multiple_select
import com.divinelink.core.ui.resources.core_ui_delete
import com.divinelink.core.ui.resources.core_ui_deselect_all
import com.divinelink.core.ui.resources.core_ui_select_all
import com.divinelink.feature.lists.resources.Res
import com.divinelink.feature.lists.resources.feature_lists_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun BoxScope.MultipleSelectHeader(
  modifier: Modifier = Modifier,
  visible: Boolean,
  selectedItems: List<MediaReference>,
  totalItemCount: Int,
  onRemoveAction: () -> Unit,
  onSelectAll: () -> Unit,
  onDeselectAll: () -> Unit,
  onDismiss: () -> Unit,
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn(tween(easing = EaseIn)),
    exit = fadeOut(tween(easing = EaseOut)),
    modifier = modifier
      .align(Alignment.BottomCenter)
      .padding(bottom = LocalBottomNavigationPadding.current + MaterialTheme.dimensions.keyline_16),
    label = "Show multiple selection header animation",
  ) {
    Card(
      modifier = Modifier.testTag(TestTags.Components.MULTIPLE_SELECT_HEADER),
    ) {
      FlowRow(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        horizontalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
          vertical = MaterialTheme.dimensions.keyline_4,
          horizontal = MaterialTheme.dimensions.keyline_8,
        ),
      ) {
        Text(
          text = stringResource(
            Res.string.feature_lists_selected,
            selectedItems.size,
            totalItemCount,
          ),
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            onClick = onRemoveAction,
            enabled = selectedItems.isNotEmpty(),
          ) {
            Icon(
              imageVector = Icons.Rounded.DeleteForever,
              contentDescription = stringResource(UiString.core_ui_delete),
              tint = LocalContentColor.current,
            )
          }

          val allNotSelected = selectedItems.size < totalItemCount

          IconButton(
            onClick = if (allNotSelected) {
              onSelectAll
            } else {
              onDeselectAll
            },
          ) {
            Icon(
              imageVector = if (allNotSelected) Icons.Rounded.SelectAll else Icons.Rounded.Deselect,
              contentDescription = if (allNotSelected) {
                stringResource(UiString.core_ui_select_all)
              } else {
                stringResource(UiString.core_ui_deselect_all)
              },
              tint = LocalContentColor.current,
            )
          }

          IconButton(
            onClick = onDismiss,
          ) {
            Icon(
              imageVector = Icons.Rounded.Close,
              contentDescription = stringResource(UiString.core_ui_close_multiple_select),
            )
          }
        }
      }
    }
  }
}
