package com.divinelink.core.ui.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.extension.getColorRating

@Composable
fun StarRatingItem(
  modifier: Modifier = Modifier,
  rating: Int,
) {
  val color = rating.toDouble().getColorRating()

  Row(
    modifier = modifier
      .padding(
        vertical = MaterialTheme.dimensions.keyline_6,
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
  ) {
    Icon(
      imageVector = Icons.Rounded.Star,
      contentDescription = null,
      tint = color,
    )

    Text(
      text = rating.toString(),
      style = MaterialTheme.typography.titleSmall,
      color = color,
    )
  }
}

@Composable
@Previews
fun StarRatingItemPreview() {
  AppTheme {
    Surface {
      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4)) {
        for (rating in -1..10) {
          StarRatingItem(rating = rating)
        }
      }
    }
  }
}
