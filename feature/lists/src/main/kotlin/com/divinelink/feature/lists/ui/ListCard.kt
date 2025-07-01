package com.divinelink.feature.lists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.coil.ListItemBackdropImage
import com.divinelink.feature.lists.R

@Composable
fun ListCard(
  modifier: Modifier = Modifier,
  listItem: ListItem,
  onClick: (ListItem) -> Unit,
) {
  Card(
    modifier = modifier
      .wrapContentHeight(),
    onClick = { onClick(listItem) },
  ) {
    Box {
      ListItemBackdropImage(
        modifier = Modifier,
        contentScale = ContentScale.Fit,
        url = ApiConstants.TMDB_BACKDROP_URL + listItem.backdropPath,
      )

      Column(
        modifier = Modifier.align(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = listItem.name,
          style = MaterialTheme.typography.titleMedium,
        )

        Text(
          text = pluralStringResource(
            R.plurals.feature_lists_item_count,
            listItem.numberOfItems,
            listItem.numberOfItems,
          ),
          style = MaterialTheme.typography.bodyMedium,
        )
      }
      VisibilityBadge(
        modifier = Modifier
          .padding(MaterialTheme.dimensions.keyline_8)
          .align(Alignment.TopEnd),
        isPublic = listItem.public,
      )
    }
  }
}

@Composable
private fun VisibilityBadge(
  modifier: Modifier = Modifier,
  isPublic: Boolean,
) {
  Box(
    modifier = modifier
      .wrapContentHeight()
      .graphicsLayer { alpha = 0.8f }
      .background(
        color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
        shape = MaterialTheme.shapes.extraLarge,
      )
      .padding(MaterialTheme.dimensions.keyline_4),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_4),
      text = if (isPublic) {
        stringResource(R.string.feature_lists_public)
      } else {
        stringResource(R.string.feature_lists_private)
      },
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary,
    )
  }
}

@Composable
@Previews
fun ListCardPreview() {
  AppTheme {
    Surface {
      ListCard(
        listItem = ListItemFactory.nonPrivateList(),
        onClick = {},
      )
    }
  }
}
