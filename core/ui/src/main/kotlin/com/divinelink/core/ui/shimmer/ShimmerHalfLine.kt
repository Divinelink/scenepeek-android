package com.divinelink.core.ui.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.Constants
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.valentinilk.shimmer.shimmer

@Composable
@Previews
fun ShimmerHalfLine(
  modifier: Modifier = Modifier,
  tag: String = Constants.String.EMPTY,
) {
  Row(
    modifier = Modifier
      .testTag(TestTags.Shimmer.HALF_LINE.format(tag))
      .fillMaxWidth(),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.5f)
        .height(MaterialTheme.dimensions.keyline_16)
        .shimmer()
        .clip(MaterialTheme.shapes.extraSmall)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}

@Composable
@Previews
fun ShimmerLine(
  modifier: Modifier = Modifier,
  tag: String = Constants.String.EMPTY,
) {
  Row(
    modifier = modifier
      .testTag(TestTags.Shimmer.LINE.format(tag))
      .fillMaxWidth(),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(MaterialTheme.dimensions.keyline_16)
        .shimmer()
        .clip(MaterialTheme.shapes.extraSmall)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}
