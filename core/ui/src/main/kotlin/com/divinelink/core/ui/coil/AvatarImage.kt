package com.divinelink.core.ui.coil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

object AvatarImage {

  @Composable
  fun Small(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    username: String,
  ) {
    AvatarComponent(
      modifier = modifier.size(MaterialTheme.dimensions.keyline_48),
      fallbackSize = MaterialTheme.dimensions.keyline_32,
      avatarUrl = avatarUrl,
      username = username,
    )
  }

  @Composable
  fun Large(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    username: String,
  ) {
    AvatarComponent(
      modifier = modifier.size(MaterialTheme.dimensions.keyline_96),
      fallbackSize = MaterialTheme.dimensions.keyline_56,
      avatarUrl = avatarUrl,
      username = username,
    )
  }
}

@Composable
private fun AvatarComponent(
  modifier: Modifier = Modifier,
  fallbackSize: Dp,
  avatarUrl: String?,
  username: String,
) {
  Box(
    modifier = modifier
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.primaryContainer),
    contentAlignment = Alignment.Center,
  ) {
    when {
      avatarUrl != null -> CoilImage(
        modifier = Modifier,
        url = avatarUrl,
        contentScale = ContentScale.Crop,
        fallbackSize = fallbackSize,
      )
      username.isNotEmpty() -> InitialsAvatar(
        modifier = modifier,
        username = username,
      )
      else -> FallbackAvatar(fallbackSize = fallbackSize)
    }
  }
}

@Composable
private fun InitialsAvatar(
  modifier: Modifier,
  username: String,
) {
  Box(
    modifier = modifier.clip(CircleShape),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = username.first().uppercase(),
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
  }
}

@Composable
private fun CoilImage(
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
  url: String?,
  fallbackSize: Dp,
  fallbackImage: ImageVector = Icons.Outlined.Person,
  color: Color = MaterialTheme.colorScheme.primary,
) {
  var useFallback by remember { mutableStateOf(false) }

  if (useFallback) {
    FallbackAvatar(
      fallbackImage = fallbackImage,
      color = color,
      fallbackSize = fallbackSize,
    )
  } else {
    AsyncImage(
      modifier = modifier.clip(CircleShape),
      model = ImageRequest.Builder(LocalContext.current)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .data(url)
        .crossfade(true)
        .build(),
      onError = { useFallback = true },
      contentDescription = stringResource(id = R.string.core_ui_avatar_image_placeholder),
      contentScale = contentScale,
    )
  }
}

@Composable
private fun FallbackAvatar(
  modifier: Modifier = Modifier,
  fallbackSize: Dp,
  fallbackImage: ImageVector = Icons.Outlined.Person,
  color: Color = MaterialTheme.colorScheme.primary,
) {
  Icon(
    modifier = modifier
      .clip(CircleShape)
      .size(fallbackSize),
    imageVector = fallbackImage,
    tint = color,
    contentDescription = stringResource(id = R.string.core_ui_avatar_image_placeholder),
  )
}

@Previews
@Composable
fun AvatarImagePreview() {
  AppTheme {
    Surface {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        AvatarImage.Large(
          avatarUrl = "mwR7rFHoDcobAx1i61I3skzMW3U.jpg",
          username = "Jenifer Reeves",
        )

        AvatarImage.Small(
          avatarUrl = "mwR7rFHoDcobAx1i61I3skzMW3U.jpg",
          username = "Jenifer Reeves",
        )
      }
    }
  }
}

@Previews
@Composable
fun AvatarImageWithInitialsPreview() {
  AppTheme {
    Surface {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        AvatarImage.Large(
          avatarUrl = null,
          username = "Jenifer Reeves",
        )

        AvatarImage.Small(
          avatarUrl = null,
          username = "Jenifer Reeves",
        )
      }
    }
  }
}
