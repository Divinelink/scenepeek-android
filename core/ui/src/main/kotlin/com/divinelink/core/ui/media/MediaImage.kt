package com.divinelink.core.ui.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.rating.DiscreetRatingItem

@Composable
fun MediaImage(
  media: MediaItem.Media,
  modifier: Modifier = Modifier,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .wrapContentHeight(),
  ) {
    MovieImage(
      path = media.posterPath,
    )

    if (media.isFavorite == true) {
      Surface(
        modifier = Modifier
          .offset(
            x = -MaterialTheme.dimensions.keyline_8,
            y = MaterialTheme.dimensions.keyline_8,
          )
          .size(MaterialTheme.dimensions.keyline_20)
          .align(Alignment.BottomEnd),
        color = MaterialTheme.colors.crimsonRed,
        shape = CircleShape,
      ) {
        Icon(
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_4),
          imageVector = Icons.Default.Favorite,
          tint = Color.White,
          contentDescription = null,
        )
      }
    }

    Column(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(
          end = MaterialTheme.dimensions.keyline_4,
          top = MaterialTheme.dimensions.keyline_4,
        ),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      media.accountRating?.let {
        DiscreetRatingItem(
          rating = it.toDouble(),
          isAccountRate = true,
        )
      }

      DiscreetRatingItem(
        rating = media.voteAverage,
        isAccountRate = false,
      )
    }
  }
}
