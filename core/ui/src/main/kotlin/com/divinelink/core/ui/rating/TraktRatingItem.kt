@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.commons.extensions.toShortString
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews

@Composable
fun TraktRatingItem(
  modifier: Modifier = Modifier,
  rating: Double?,
  voteCount: Int?,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Icon(
      modifier = Modifier.size(MaterialTheme.dimensions.keyline_32),
      imageVector = Icons.Default.Favorite,
      tint = Color(red = 168, green = 25, blue = 24),
      contentDescription = null,
    )

    Column {
      Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "${rating?.times(10)?.toInt()}%",
          style = MaterialTheme.typography.labelLarge,
          fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
      }

      Text(
        text = (voteCount?.toShortString() + " votes"),
        style = MaterialTheme.typography.labelSmall,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f),
      )
    }
  }
}

@Previews
@Composable
fun TraktRatingItemPreview() {
  AppTheme {
    Surface {
      Column {
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
          TraktRatingItem(
            modifier = Modifier,
            rating = 7.7,
            voteCount = 2_345,
          )
        }
      }
    }
  }
}
