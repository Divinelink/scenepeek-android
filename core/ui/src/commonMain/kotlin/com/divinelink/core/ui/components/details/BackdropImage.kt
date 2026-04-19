package com.divinelink.core.ui.components.details

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.coil.platformContext
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberConstants
import com.divinelink.core.ui.resources.core_ui_backdrop_image_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackdropImage(
  path: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.FillWidth,
  onBackdropLoaded: () -> Unit,
) {
  val constants = rememberConstants()

  AsyncImage(
    modifier = modifier
      .bottomFadeOut()
      .conditional(
        condition = path?.isNotBlank() == true,
        ifTrue = { aspectRatio(16 / 9f) },
        ifFalse = { height(MaterialTheme.dimensions.keyline_64 * 2) },
      )
      .fillMaxWidth(),
    model = ImageRequest.Builder(platformContext())
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(constants.backdropUrl + path)
      .crossfade(true)
      .build(),
    onSuccess = { onBackdropLoaded() },
    contentDescription = stringResource(UiString.core_ui_backdrop_image_placeholder),
    contentScale = contentScale,
  )
}

private fun Modifier.bottomFadeOut() = this.then(
  Modifier
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
      drawContent()
      drawRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color.Black, Color.Transparent),
          startY = size.height * 0.30f,
          endY = size.height,
        ),
        blendMode = BlendMode.DstIn,
      )
    },
)
