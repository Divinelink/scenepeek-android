@file:Suppress("MagicNumber")

package com.divinelink.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.designsystem.theme.shape

@Composable
fun MovieImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  errorPlaceHolder: Painter = painterResource(R.drawable.core_ui_ic_movie_placeholder),
  path: String?,
) {
  AsyncImage(
    modifier = modifier
      .clip(MaterialTheme.shape.medium)
      .aspectRatio((2f / 3f)),
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(ApiConstants.TMDB_IMAGE_URL + path)
      .crossfade(true)
      .build(),
    error = errorPlaceHolder,
    contentDescription = stringResource(id = R.string.core_ui_movie_image_placeholder),
    contentScale = contentScale,
  )
}
