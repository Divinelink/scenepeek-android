package com.divinelink.feature.details.media.ui.ratings

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.provider.RatingCountParameterProvider

@Previews
@Composable
fun AllRatingsContentScreenshots(
  @PreviewParameter(RatingCountParameterProvider::class) ratingCount: RatingCount,
) {
  AppTheme {
    Surface {
      AllRatingsContent(
        onClick = {},
        ratingCount = ratingCount,
      )
    }
  }
}
