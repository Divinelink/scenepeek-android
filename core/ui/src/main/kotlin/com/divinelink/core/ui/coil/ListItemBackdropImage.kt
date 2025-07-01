package com.divinelink.core.ui.coil

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.ui.R

@Composable
fun ListItemBackdropImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.FillWidth,
  url: String?,
) {
  AsyncImage(
    modifier = modifier
      .aspectRatio(16f / 9f)
      .graphicsLayer { alpha = 0.4f },
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(url)
      .crossfade(true)
      .build(),
    error = painterResource(R.drawable.core_ui_ic_image),
    contentDescription = stringResource(id = R.string.core_ui_backdrop_image_placeholder),
    contentScale = contentScale,
  )
}
