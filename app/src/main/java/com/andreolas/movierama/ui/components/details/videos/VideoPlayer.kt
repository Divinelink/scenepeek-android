package com.andreolas.movierama.ui.components.details.videos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.ui.VideoState
import com.andreolas.movierama.ui.theme.AppTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

@Composable
fun YoutubePlayer(
    modifier: Modifier = Modifier,
    trailer: Video,
    onStateChange: (VideoState) -> Unit,
    state: VideoState,
) {
    val context = LocalContext.current
    val options = IFramePlayerOptions.Builder().controls(0).build()
    val startSeconds = remember { mutableStateOf(0f) }

    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false
            val listener = object : AbstractYouTubePlayerListener() {
                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    startSeconds.value = second
                }

                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val playerController = DefaultPlayerUiController(
                        youTubePlayerView = this@apply,
                        youTubePlayer = youTubePlayer,
                    ).apply {
                        showBufferingProgress(true)
                    }
                    setCustomPlayerUi(playerController.rootView)
                    when (state) {
                        VideoState.PLAYING -> {
                            youTubePlayer.loadVideo(
                                videoId = trailer.key,
                                startSeconds = startSeconds.value,
                            )
                        }

                        VideoState.PAUSED -> {
                            // Intentionally Blank.
                        }

                        VideoState.NOT_PRESENT -> {
                            youTubePlayer.cueVideo(
                                videoId = trailer.key,
                                startSeconds = 0f,
                            )
                            playerController.apply {
                                showSeekBar(false)
                                showYouTubeButton(false)
                                showCurrentTime(false)
                                showFullscreenButton(false)
                                showDuration(false)
                            }
                        }
                    }
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState,
                ) {
                    when (state) {
                        PlayerConstants.PlayerState.PLAYING -> onStateChange(VideoState.PLAYING)
                        PlayerConstants.PlayerState.PAUSED -> onStateChange(VideoState.PAUSED)
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
                .testTag("VideoPlayer"),
            factory = {
                youtubePlayer
            }
        )
    ) {
        onDispose {
            youtubePlayer.release()
        }
    }
}

@Preview
@Composable
fun VideoPlayerPreview() {
    AppTheme {
        YoutubePlayer(
            trailer = Video(
                id = "",
                name = "",
                officialTrailer = false,
                site = null,
                key = "",
            ),
            onStateChange = { },
            state = VideoState.NOT_PRESENT,
        )
    }
}
