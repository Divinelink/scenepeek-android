package com.divinelink.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.skeleton.MediaItemSkeleton
import com.divinelink.feature.home.HomeForm

@Composable
fun MediaRowContent(
  form: HomeForm.Data<MediaItem>,
  onLoadMore: () -> Unit,
  onRetry: () -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val state = rememberLazyListState()

  state.EndlessScrollHandler(buffer = 4) { onLoadMore() }

  LazyRow(
    state = state,
    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    items(
      items = form.media,
      key = { it.uniqueIdentifier },
    ) { item ->
      when (item) {
        is MediaItem.Media -> MediaItem(
          modifier = Modifier
            .animateItem(),
          media = item,
          minLines = 3,
          onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
          onLongClick = {
            onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString()))
          },
        )
        is MediaItem.Person -> Unit
        MediaItem.Unknown -> Unit
      }
    }

    if (form.canLoadMore || form.isLoading) {
      items(count = 2) {
        MediaItemSkeleton(
          modifier = Modifier
            .animateItem(),
        )
      }
    }

    if (form.hasError) {
      item {
        MediaRowError(
          modifier = Modifier
            .animateItem(),
          onClick = onRetry,
        )
      }
    }
  }
}
