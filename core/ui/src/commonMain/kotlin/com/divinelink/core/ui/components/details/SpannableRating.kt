package com.divinelink.core.ui.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.extension.getColorRating

@Composable
fun SpannableRating(
  modifier: Modifier = Modifier,
  text: String,
  rating: Int,
) {
  val color = rating.toDouble().getColorRating()

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = text,
      lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
      color = MaterialTheme.colorScheme.onSurface,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))

    Text(
      modifier = Modifier.testTag(TestTags.Details.YOUR_RATING),
      text = rating.toString(),
      color = color,
      style = MaterialTheme.typography.headlineMedium,
      textAlign = TextAlign.Center,
    )
  }
}

@Previews
@Composable
fun SpannableRatingPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        SpannableRating(
          text = "Your rating",
          rating = 0,
        )
        SpannableRating(
          text = "Your rating",
          rating = 1,
        )
        SpannableRating(
          text = "Your rating",
          rating = 5,
        )
        SpannableRating(
          text = "Your rating",
          rating = 8,
        )
      }
    }
  }
}
