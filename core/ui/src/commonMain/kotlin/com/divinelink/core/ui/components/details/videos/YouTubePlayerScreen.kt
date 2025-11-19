package com.divinelink.core.ui.components.details.videos

import androidx.compose.runtime.Composable

@Composable
expect fun YouTubePlayerScreen(
  videoId: String,
  onBack: () -> Unit,
)
