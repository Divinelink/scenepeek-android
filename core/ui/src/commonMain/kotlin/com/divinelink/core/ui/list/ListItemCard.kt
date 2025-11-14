package com.divinelink.core.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.coil.ListItemBackdropImage
import com.divinelink.core.ui.components.VisibilityBadge
import com.divinelink.core.ui.core_ui_added_on_list
import com.divinelink.core.ui.core_ui_item_count
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ListItemCard(
  modifier: Modifier = Modifier,
  listItem: ListItem,
  isAdded: Boolean,
  onClick: (ListItem) -> Unit,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = { onClick(listItem) },
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(
      containerColor = Color.Transparent,
    ),
  ) {
    Row(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_8)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ListItemBackdropImage(
        modifier = Modifier.weight(3f),
        url = listItem.backdropPath,
      )

      Column(
        modifier = Modifier.weight(5f),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          text = listItem.name,
          style = MaterialTheme.typography.titleSmall,
        )

        Text(
          text = pluralStringResource(
            UiPlurals.core_ui_item_count,
            listItem.numberOfItems,
            listItem.numberOfItems,
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        AnimatedVisibility(isAdded) {
          Icon(
            imageVector = Icons.Default.Done,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(UiString.core_ui_added_on_list, listItem.name),
          )
        }

        VisibilityBadge(
          isPublic = listItem.public,
        )
      }
    }
  }
}
