package com.divinelink.feature.details.media.ui.ratings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.valentinilk.shimmer.shimmer

@Composable
fun RatingContentShimmer(
  modifier: Modifier = Modifier,
  withVoteCount: Boolean,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = " ",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_24)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )

    if (withVoteCount) {
      Text(
        text = " ",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
          .width(MaterialTheme.dimensions.keyline_48)
          .shimmer()
          .clip(MaterialTheme.shapes.small)
          .background(MaterialTheme.colorScheme.onSurfaceVariant),
      )
    }
  }
}

@Previews
@Composable
private fun RatingSkeletonPreview() {
  AppTheme {
    RatingContentShimmer(withVoteCount = true)
  }
}
