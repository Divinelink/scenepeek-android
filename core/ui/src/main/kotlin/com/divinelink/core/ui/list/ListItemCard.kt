package com.divinelink.core.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.coil.ListItemBackdropImage
import com.divinelink.core.ui.components.VisibilityBadge

@Composable
@Previews
fun ListItemCard(
  modifier: Modifier = Modifier,
  listItem: ListItem = ListItemFactory.nonPrivateList(),
  onClick: (ListItem) -> Unit = {},
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
        .padding(MaterialTheme.dimensions.keyline_16)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      ListItemBackdropImage(
        modifier = Modifier.weight(3f),
        url = ApiConstants.TMDB_BACKDROP_URL + listItem.backdropPath,
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
            com.divinelink.core.ui.R.plurals.core_ui_item_count,
            listItem.numberOfItems,
            listItem.numberOfItems,
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      VisibilityBadge(
        isPublic = listItem.public,
      )
    }
  }
}
