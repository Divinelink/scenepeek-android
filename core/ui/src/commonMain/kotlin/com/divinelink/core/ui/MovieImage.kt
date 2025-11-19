@file:Suppress("MagicNumber")

package com.divinelink.core.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.coil.platformContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MovieImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  errorPlaceHolder: Painter = painterResource(UiDrawable.core_ui_ic_movie_placeholder),
  path: String?,
) {
  val constants = rememberConstants()
  var isError by remember { mutableStateOf(false) }

  AsyncImage(
    modifier = modifier
      .clip(MaterialTheme.shape.medium)
      .aspectRatio((2f / 3f)),
    model = ImageRequest.Builder(platformContext())
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(constants.imageUrl + path)
      .crossfade(true)
      .build(),
    onError = { isError = true },
    onSuccess = { isError = false },
    error = errorPlaceHolder,
    colorFilter = if (isError) {
      ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer)
    } else {
      null
    },
    contentDescription = stringResource(UiString.core_ui_movie_image_placeholder),
    contentScale = contentScale,
  )
}
