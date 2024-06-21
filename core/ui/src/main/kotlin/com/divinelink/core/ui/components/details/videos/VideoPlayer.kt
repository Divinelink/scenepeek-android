package com.divinelink.core.ui.components.details.videos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.ui.Previews
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

const val VIDEO_PLAYER_TAG = "VIDEO_PLAYER_TAG"

@Composable
fun YoutubePlayer(
  modifier: Modifier = Modifier,
  video: Video,
  onStateChange: (VideoState) -> Unit,
) {
  val context = LocalContext.current
  val options = IFramePlayerOptions.Builder().controls(0).build()
  val startSeconds = rememberSaveable { mutableStateOf(0f) }
  val playerState = rememberSaveable { mutableStateOf(VideoState.NOT_PRESENT) }
  val playerController = remember { mutableStateOf<DefaultPlayerUiController?>(null) }

  val youtubePlayerView = remember {
    YouTubePlayerView(context).apply {
      enableAutomaticInitialization = false
      val listener = object : AbstractYouTubePlayerListener() {
        override fun onCurrentSecond(
          youTubePlayer: YouTubePlayer,
          second: Float,
        ) {
          startSeconds.value = second
        }

        override fun onReady(youTubePlayer: YouTubePlayer) {
          playerController.value = DefaultPlayerUiController(
            youTubePlayerView = this@apply,
            youTubePlayer = youTubePlayer,
          ).apply {
            setCustomPlayerUi(rootView)
            showSeekBar(false)
            showYouTubeButton(false)
            showCurrentTime(false)
            showFullscreenButton(false)
            showDuration(false)
          }
          if (playerState.value == VideoState.PLAYING) {
            youTubePlayer.loadVideo(
              videoId = video.key,
              startSeconds = startSeconds.value,
            )
          } else if (
            playerState.value == VideoState.PAUSED ||
            playerState.value == VideoState.NOT_PRESENT
          ) {
            youTubePlayer.cueVideo(
              videoId = video.key,
              startSeconds = startSeconds.value,
            )
          }
        }

        override fun onStateChange(
          youTubePlayer: YouTubePlayer,
          state: PlayerConstants.PlayerState,
        ) {
          when (state) {
            PlayerConstants.PlayerState.PLAYING -> {
              onStateChange(VideoState.PLAYING)
              playerState.value = VideoState.PLAYING
              playerController.value?.apply {
                showSeekBar(true)
                showYouTubeButton(true)
                showCurrentTime(true)
                showDuration(true)
              }
            }

            PlayerConstants.PlayerState.PAUSED -> {
              onStateChange(VideoState.PAUSED)
              playerState.value = VideoState.PAUSED
            }

            else -> {
              // Intentionally Blank.
            }
          }
        }
      }
      initialize(listener, options)
    }
  }

  DisposableEffect(
    AndroidView(
      modifier = modifier
        .fillMaxSize()
        .testTag(VIDEO_PLAYER_TAG),
      factory = {
        youtubePlayerView
      },
    ),
  ) {
    onDispose {
      youtubePlayerView.release()
    }
  }
}

enum class VideoState {
  PLAYING,
  PAUSED,
  NOT_PRESENT,
}

@Previews
@Composable
private fun VideoPlayerPreview() {
  AppTheme {
    YoutubePlayer(
      video = Video(
        id = "",
        name = "",
        officialTrailer = false,
        site = null,
        key = "",
      ),
      onStateChange = { },
    )
  }
}
