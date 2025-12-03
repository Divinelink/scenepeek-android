package com.divinelink.core.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
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
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.resources.Res
import com.divinelink.core.ui.resources.core_ui_mark_as_favorite_button_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoriteButton(
  modifier: Modifier = Modifier,
  isFavorite: Boolean,
  inactiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  onClick: () -> Unit,
) {
  val color by animateColorAsState(
    targetValue = when (isFavorite) {
      true -> MaterialTheme.colors.redHighlight
      false -> inactiveColor
    },
    label = "Like button color",
  )

  Box(
    modifier = modifier
      .padding(MaterialTheme.dimensions.keyline_4)
      .clip(MaterialTheme.shape.rounded)
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
          Res.string.core_ui_mark_as_favorite_button_content_description,
        ),
      )
    }
  }
}
