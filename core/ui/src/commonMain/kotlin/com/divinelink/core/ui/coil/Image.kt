package com.divinelink.core.ui.coil

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.model.ImageQuality

@Composable
fun Image(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  path: String?,
  quality: ImageQuality,
) {
  AsyncImage(
    modifier = modifier,
    model = ImageRequest.Builder(platformContext())
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(quality.url + path)
      .crossfade(true)
      .build(),
    contentDescription = null,
    contentScale = contentScale,
  )
}
