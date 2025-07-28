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
import androidx.compose.material.icons.rounded.Deselect
import androidx.compose.material.icons.rounded.MoreVert
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
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.feature.lists.R

@Composable
fun BoxScope.MultipleSelectHeader(
  modifier: Modifier = Modifier,
  visible: Boolean,
  selectedItems: List<Int>,
  totalItemCount: Int,
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
    Card {
      FlowRow(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        horizontalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
          vertical = MaterialTheme.dimensions.keyline_8,
          horizontal = MaterialTheme.dimensions.keyline_4,
        ),
      ) {
        Text(
          text = stringResource(
            R.string.feature_lists_selected,
            selectedItems.size,
            totalItemCount,
          ),
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_8),
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(
            onClick = {
              // Show Modal
            },
          ) {
            Icon(
              imageVector = Icons.Rounded.MoreVert,
              contentDescription = null,
              tint = LocalContentColor.current,
            )
          }

          val allSelected = selectedItems.size < totalItemCount

          IconButton(
            onClick = if (allSelected) {
              onSelectAll
            } else {
              onDeselectAll
            },
          ) {
            Icon(
              imageVector = if (allSelected) Icons.Rounded.SelectAll else Icons.Rounded.Deselect,
              contentDescription = null,
              tint = LocalContentColor.current,
            )
          }

          IconButton(
            onClick = onDismiss,
          ) {
            Icon(
              imageVector = Icons.Rounded.Close,
              contentDescription = null,
            )
          }
        }
      }
    }
  }
}
