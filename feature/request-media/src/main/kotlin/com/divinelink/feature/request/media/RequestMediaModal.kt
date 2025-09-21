package com.divinelink.feature.request.media

import androidx.compose.runtime.Composable
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.request.media.movie.RequestMovieModal
import com.divinelink.feature.request.media.tv.RequestSeasonsModal

@Composable
fun RequestMediaModal(
  isEditMode: Boolean,
  mediaType: MediaType,
  media: MediaItem.Media?,
  seasons: List<Season>,
  onDismissRequest: () -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  if (media == null) return

  when (mediaType) {
    MediaType.TV -> RequestSeasonsModal(
      isEditMode = isEditMode,
      seasons = seasons,
      media = media,
      onDismissRequest = onDismissRequest,
      onNavigate = onNavigate,
      onUpdateMediaInfo = onUpdateMediaInfo,
    )
    MediaType.MOVIE -> RequestMovieModal(
      isEditMode = isEditMode,
      media = media,
      onDismissRequest = onDismissRequest,
      onUpdateMediaInfo = onUpdateMediaInfo,
      onNavigate = onNavigate,
    )
    else -> {
      // Do nothing
    }
  }
}
