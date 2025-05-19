package com.divinelink.core.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.coreRedHighlight
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape

@Composable
fun FavoriteButton(
  modifier: Modifier = Modifier,
  isFavorite: Boolean,
  transparentBackground: Boolean = false,
  inactiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  onClick: () -> Unit,
) {
  val color by animateColorAsState(
    targetValue = when (isFavorite) {
      true -> coreRedHighlight
      false -> inactiveColor
    },
    label = "Like button color",
  )

  val backgroundColor = when (transparentBackground) {
    true -> Color.Transparent
    false -> MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
  }

  Box(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_4)
      .clip(MaterialTheme.shape.rounded)
      .background(color = backgroundColor)
      .clickable { onClick() }
      .size(MaterialTheme.dimensions.keyline_40),
  ) {
    Crossfade(
      modifier = Modifier.align(Alignment.Center),
      targetState = isFavorite,
      label = "Like button",
    ) { favorite ->
      val image = when (favorite) {
        true -> Icons.Default.Favorite
        false -> Icons.Default.FavoriteBorder
      }
      Icon(
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_26),
        imageVector = image,
        tint = color,
        contentDescription = stringResource(
          R.string.core_ui_mark_as_favorite_button_content_description,
        ),
      )
    }
  }
}
