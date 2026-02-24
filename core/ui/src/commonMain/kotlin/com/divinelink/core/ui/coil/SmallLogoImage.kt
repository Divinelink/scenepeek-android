package com.divinelink.core.ui.coil

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.ImageQuality
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.debouncedClickable

@Composable
fun SmallLogoImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  path: String?,
  onClick: (String) -> Unit = {},
) {
  var isError by remember { mutableStateOf(false) }
  val context = platformContext()

  AsyncImage(
    modifier = modifier
      .size(MaterialTheme.dimensions.keyline_32)
      .clip(MaterialTheme.shape.medium)
      .conditional(
        condition = path != null,
        ifTrue = { debouncedClickable { onClick(path ?: "") } },
      ),
    model = ImageRequest.Builder(context)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(ImageQuality.QUALITY_342.url + path)
      .build(),
    onError = { isError = true },
    onSuccess = { isError = false },
    colorFilter = if (isError) {
      ColorFilter.tint(MaterialTheme.colorScheme.surfaceContainer)
    } else {
      null
    },
    contentDescription = null,
    contentScale = contentScale,
  )
}
