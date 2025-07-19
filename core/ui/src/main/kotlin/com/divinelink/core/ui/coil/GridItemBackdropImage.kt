package com.divinelink.core.ui.coil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.ui.R

@Composable
fun GridItemBackdropImage(
  modifier: Modifier = Modifier,
  url: String?,
) {
  Box(
    modifier = modifier
      .clip(MaterialTheme.shapes.medium)
      .background(MaterialTheme.colorScheme.surfaceVariant),
  ) {
    AsyncImage(
      modifier = Modifier
        .aspectRatio(16f / 9f)
        .graphicsLayer { alpha = 0.4f },
      model = ImageRequest.Builder(LocalContext.current)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .data(ApiConstants.TMDB_BACKDROP_URL + url)
        .crossfade(true)
        .build(),
      error = painterResource(R.drawable.core_ui_ic_image),
      contentDescription = stringResource(id = R.string.core_ui_backdrop_image_placeholder),
      contentScale = ContentScale.Fit,
    )
  }
}
