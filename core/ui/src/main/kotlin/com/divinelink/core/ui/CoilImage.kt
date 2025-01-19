package com.divinelink.core.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CoilImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  url: String?,
  error: Int = R.drawable.core_ui_ic_person_placeholder,
  fallback: Int = R.drawable.core_ui_ic_person_placeholder,
) {
  AsyncImage(
    modifier = modifier.clip(CircleShape),
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(url)
      .crossfade(true)
      .build(),
    error = painterResource(error),
    fallback = painterResource(fallback),
    contentDescription = stringResource(id = R.string.core_ui_avatar_image_placeholder),
    contentScale = contentScale,
  )
}
