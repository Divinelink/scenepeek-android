package com.divinelink.core.ui.coil

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.ui.R

@Composable
fun OpaqueBackdropImage(
  path: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.FillWidth,
) {
  AsyncImage(
    modifier = modifier
      .graphicsLayer {
        alpha = 0.2f
      }
      .fillMaxWidth(),
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(ApiConstants.TMDB_BACKDROP_URL + path)
      .crossfade(true)
      .build(),
    contentDescription = stringResource(id = R.string.core_ui_backdrop_image_placeholder),
    contentScale = contentScale,
  )
}
