package com.divinelink.core.ui.coil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_backdrop_image_placeholder
import com.divinelink.core.ui.core_ui_ic_image
import com.divinelink.core.ui.rememberConstants
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GridItemBackdropImage(
  modifier: Modifier = Modifier,
  url: String?,
) {
  val constants = rememberConstants()
  var isError by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .clip(MaterialTheme.shapes.medium)
      .background(MaterialTheme.colorScheme.surfaceVariant),
  ) {
    AsyncImage(
      modifier = Modifier
        .aspectRatio(16f / 9f)
        .graphicsLayer { alpha = 0.4f },
      model = ImageRequest.Builder(platformContext())
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .data(constants.imageUrl + url)
        .crossfade(true)
        .build(),
      error = painterResource(UiDrawable.core_ui_ic_image),
      onError = { isError = true },
      onSuccess = { isError = false },
      colorFilter = if (isError) {
        ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainerLowest)
      } else {
        null
      },
      contentDescription = stringResource(UiString.core_ui_backdrop_image_placeholder),
      contentScale = ContentScale.Fit,
    )
  }
}
