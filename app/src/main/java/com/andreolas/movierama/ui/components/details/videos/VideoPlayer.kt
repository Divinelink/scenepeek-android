package com.andreolas.movierama.ui.components.details.videos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.ui.theme.AppTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

@Composable
fun YoutubePlayer(
    modifier: Modifier = Modifier,
    trailer: Video,
) {
    val context = LocalContext.current
    val options = IFramePlayerOptions.Builder().controls(0).build()

    val youtubePlayer = remember {
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false
            val listener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val playerController = DefaultPlayerUiController(
                        youTubePlayerView = this@apply,
                        youTubePlayer = youTubePlayer,
                    ).apply {
                        showBufferingProgress(false)
                    }
                    setCustomPlayerUi(playerController.rootView)
                    youTubePlayer.cueVideo(
                        videoId = trailer.key,
                        startSeconds = 0f,
                    )
                }
            }
            initialize(listener, options)
        }
    }

    DisposableEffect(
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .testTag("VideoPlayer"),
            factory = {
                youtubePlayer
            }
        )
    ) {
        onDispose {
            // release player when no longer needed
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
            )
        )
    }
}
