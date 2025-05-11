package com.divinelink.feature.details.media.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.ui.components.details.videos.VideoState
import com.divinelink.core.ui.components.details.videos.YoutubePlayer

private const val MAX_WIDTH_FOR_LANDSCAPE_PLAYER = 0.55f

@Composable
fun VideoPlayerSection(
  modifier: Modifier = Modifier,
  trailer: Video,
  onVideoStateChange: (VideoState) -> Unit,
) {
  val orientation = LocalConfiguration.current.orientation
  val playerWidth = remember {
    derivedStateOf {
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        MAX_WIDTH_FOR_LANDSCAPE_PLAYER
      } else {
        1f
      }
    }
  }

  when (trailer.site) {
    VideoSite.YouTube ->
      YoutubePlayer(
        modifier = modifier
          .fillMaxWidth(playerWidth.value),
        video = trailer,
        onStateChange = { state ->
          onVideoStateChange(state)
        },
      )

    else -> {
      return
    }
  }
}
