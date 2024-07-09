@file:Suppress("MagicNumber")

package com.divinelink.core.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun CoilImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  url: String?,
) {
  AsyncImage(
    modifier = modifier.clip(CircleShape),
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(url)
      .crossfade(true)
      .build(),
    error = painterResource(R.drawable.core_ui_ic_person_placeholder),
    fallback = painterResource(R.drawable.core_ui_ic_person_placeholder),
    contentDescription = stringResource(id = R.string.core_ui_avatar_image_placeholder),
    contentScale = contentScale,
  )
}
