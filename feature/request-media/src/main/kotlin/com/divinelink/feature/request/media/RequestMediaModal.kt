package com.divinelink.feature.request.media

import androidx.compose.runtime.Composable
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
  onDismissRequest: () -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit = {},
  onUpdateRequestInfo: (JellyseerrRequest) -> Unit = {},
  onCancelRequest: (requestId: Int) -> Unit = {},
  onNavigate: (Navigation) -> Unit,
) {
  if (media == null) return

  when (mediaType) {
    MediaType.TV -> RequestSeasonsModal(
      request = request,
      media = media,
      onDismissRequest = onDismissRequest,
      onNavigate = onNavigate,
      onUpdateRequestInfo = onUpdateRequestInfo,
      onUpdateMediaInfo = onUpdateMediaInfo,
      onCancelRequest = onCancelRequest,
    )
    MediaType.MOVIE -> RequestMovieModal(
      request = request,
      media = media,
      onDismissRequest = onDismissRequest,
      onUpdateMediaInfo = onUpdateMediaInfo,
      onUpdateRequestInfo = onUpdateRequestInfo,
      onCancelRequest = onCancelRequest,
      onNavigate = onNavigate,
    )
    else -> {
      // Do nothing
    }
  }
}
