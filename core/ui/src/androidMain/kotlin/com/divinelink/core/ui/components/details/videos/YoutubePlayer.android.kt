package com.divinelink.core.ui.components.details.videos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.components.OverlayScreen
import com.divinelink.core.ui.fullscreen.FullscreenMode
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
actual fun YouTubePlayerScreen(
  videoId: String,
  onBack: () -> Unit,
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }

  OverlayScreen(
    isVisible = true,
    onDismiss = { onBack() },
    content = {
      FullscreenMode {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxSize(),
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight(),
          ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
              AndroidView(
                modifier = Modifier.aspectRatio(16f / 9f),
                factory = { context ->
                  YouTubePlayerView(context).apply {
                    lifecycleOwner.lifecycle.addObserver(this)

                    addYouTubePlayerListener(
                      object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                          youTubePlayer.loadVideo(videoId, playbackPosition)
                        }

                        override fun onCurrentSecond(
                          youTubePlayer: YouTubePlayer,
                          second: Float,
                        ) {
                          playbackPosition = second
                        }
                      },
                    )
                  }
                },
              )
            }
          }

          IconButton(
            modifier = Modifier
              .padding(MaterialTheme.dimensions.keyline_32)
              .align(Alignment.TopStart),
            onClick = onBack,
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close video player",
              tint = Color.White,
              modifier = Modifier.size(MaterialTheme.dimensions.keyline_32),
            )
          }
        }
      }
    },
  )
}
