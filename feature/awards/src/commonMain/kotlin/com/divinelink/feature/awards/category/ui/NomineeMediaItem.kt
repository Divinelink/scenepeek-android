package com.divinelink.feature.awards.category.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.credit.SmallPersonItem
import com.divinelink.core.ui.skeleton.MediaItemSkeleton

@Composable
fun LazyGridItemScope.NomineeMediaItem(
  modifier: Modifier = Modifier,
  item: LoadingUiItem<AwardNominee>,
  onNavigate: (Navigation) -> Unit,
  onClick: (MediaItem) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  when (val state = item.mediaState) {
    is ItemState.Data -> when (state.item) {
      is MediaItem.Media -> MediaItem(
        modifier = Modifier
          .animateItem(),
        media = state.item as MediaItem.Media,
        onClick = { onClick(state.item) },
        onLongClick = { onLongClick(state.item as MediaItem.Media) },
        isWinner = item.item.winner,
        contentBelowTitle = {
          item.item.countries?.let { countries ->
            Text(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimensions.keyline_4),
              text = countries.joinToString(separator = " ") { it.flag },
              style = MaterialTheme.typography.bodyMedium,
              textAlign = TextAlign.Center,
            )
          }
        },
      )
      is MediaItem.Person -> SmallPersonItem(
        person = state.item as MediaItem.Person,
        isSmall = false,
        isWinner = item.item.winner,
        onClick = { onClick(state.item) },
        footerContent = {
          item.item.winningMedia?.let { media ->
            TextButton(
              modifier = Modifier
                .widthIn(max = MaterialTheme.dimensions.shortMediaCard),
              onClick = { media.toRoute()?.let { route -> onNavigate(route) } },
            ) {
              Text(
                text = media.title ?: "",
                textAlign = TextAlign.Center,
              )
            }
          }
        },
      )
      MediaItem.Unknown -> Unit
    }

    ItemState.Loading -> MediaItemSkeleton(
      modifier = modifier,
    )

    null -> Unit
  }
}
