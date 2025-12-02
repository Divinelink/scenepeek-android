package com.divinelink.core.ui.components.details.videos

import androidx.compose.runtime.Composable
import com.divinelink.core.ui.manager.url.rememberUrlHandler

@Composable
actual fun YouTubePlayerScreen(
  videoId: String,
  onBack: () -> Unit,
) {
  val urlHandler = rememberUrlHandler()

  urlHandler.openUrl(
    url = "https://www.youtube.com/watch?v=$videoId",
  )
}
