package com.divinelink.core.ui.coil

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_backdrop_image_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackdropImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  url: String?,
) {
  AsyncImage(
    modifier = modifier,
    model = ImageRequest.Builder(platformContext())
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(url)
      .crossfade(true)
      .build(),
    contentDescription = stringResource(UiString.core_ui_backdrop_image_placeholder),
    contentScale = contentScale,
  )
}
