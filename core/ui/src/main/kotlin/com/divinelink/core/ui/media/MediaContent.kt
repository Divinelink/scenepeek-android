package com.divinelink.core.ui.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.ui.TestTags.MEDIA_LIST_TAG

@Composable
fun MediaContent(
  modifier: Modifier = Modifier,
  section: MediaSection?,
  onMediaClick: (MediaItem) -> Unit,
  onMarkAsFavoriteClick: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
) {
  if (section == null) return

  FlatMediaList(
    modifier = modifier.testTag(MEDIA_LIST_TAG),
    data = section.data,
    onItemClick = onMediaClick,
    onMarkAsFavoriteClicked = onMarkAsFavoriteClick,
    onLoadNextPage = onLoadNextPage,
    isLoading = section.shouldLoadMore,
  )
}
