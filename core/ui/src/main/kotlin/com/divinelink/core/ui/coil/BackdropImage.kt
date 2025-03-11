package com.divinelink.core.ui.coil

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.ui.R

@Composable
fun BackdropImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  url: String?,
) {
  AsyncImage(
    modifier = modifier,
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(url)
      .crossfade(true)
      .build(),
    contentDescription = stringResource(id = R.string.core_ui_backdrop_image_placeholder),
    contentScale = contentScale,
  )
}
