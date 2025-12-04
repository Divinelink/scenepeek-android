package com.divinelink.core.ui.coil

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
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.debouncedClickable
import com.divinelink.core.ui.resources.core_ui_ic_movie_placeholder
import com.divinelink.core.ui.resources.core_ui_movie_image_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PosterImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  errorPlaceHolder: Painter = painterResource(UiDrawable.core_ui_ic_movie_placeholder),
  path: String?,
  quality: ImageQuality,
  onClick: (String) -> Unit = {},
) {
  var isError by remember { mutableStateOf(false) }
  val context = platformContext()

  val placeholderPainter = rememberAsyncImagePainter(
    model = ImageRequest.Builder(context)
      .data(ImageQuality.QUALITY_342.url + path)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build(),
  )

  AsyncImage(
    modifier = modifier
      .clip(MaterialTheme.shape.medium)
      .conditional(
        condition = path != null,
        ifTrue = { debouncedClickable { onClick(path ?: "") } },
      ),
    model = ImageRequest.Builder(context)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(quality.url + path)
      .build(),
    placeholder = placeholderPainter,
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
