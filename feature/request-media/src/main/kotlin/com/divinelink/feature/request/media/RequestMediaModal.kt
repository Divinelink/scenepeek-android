package com.divinelink.feature.request.media

import androidx.compose.runtime.Composable
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.request.media.movie.RequestMovieModal
import com.divinelink.feature.request.media.tv.RequestSeasonsModal

@Composable
fun RequestMediaModal(
  request: JellyseerrRequest?,
  mediaType: MediaType,
  media: MediaItem.Media?,
  seasons: List<Season>,
  onDismissRequest: () -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit = {},
  onUpdateRequestInfo: (JellyseerrRequest) -> Unit = {},
  onNavigate: (Navigation) -> Unit,
) {
  if (media == null) return

  when (mediaType) {
    MediaType.TV -> RequestSeasonsModal(
      request = request,
      seasons = seasons,
      media = media,
      onDismissRequest = onDismissRequest,
      onNavigate = onNavigate,
      onUpdateRequestInfo = onUpdateRequestInfo,
      onUpdateMediaInfo = onUpdateMediaInfo,
    )
    MediaType.MOVIE -> RequestMovieModal(
      request = request,
      media = media,
      onDismissRequest = onDismissRequest,
      onUpdateMediaInfo = onUpdateMediaInfo,
      onUpdateRequestInfo = onUpdateRequestInfo,
      onNavigate = onNavigate,
    )
    else -> {
      // Do nothing
    }
  }
}
