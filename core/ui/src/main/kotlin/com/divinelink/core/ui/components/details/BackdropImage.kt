package com.divinelink.core.ui.components.details

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.commons.ApiConstants
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.R

@Composable
fun BackdropImage(
  path: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.FillWidth,
  onBackdropLoaded: () -> Unit,
) {
  val backdropOffset = WindowInsets.statusBars.asPaddingValues()
    .calculateTopPadding() + MaterialTheme.dimensions.keyline_64

  AsyncImage(
    modifier = modifier
      .offset(y = -backdropOffset)
      .bottomFadeOut()
      .fillMaxWidth(),
    model = ImageRequest.Builder(LocalContext.current)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .data(ApiConstants.TMDB_BACKDROP_URL + path)
      .crossfade(true)
      .build(),
    onSuccess = { onBackdropLoaded() },
    contentDescription = stringResource(id = R.string.core_ui_backdrop_image_placeholder),
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
