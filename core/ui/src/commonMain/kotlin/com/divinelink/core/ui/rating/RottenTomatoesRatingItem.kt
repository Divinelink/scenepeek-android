@file:Suppress("MagicNumber")

package com.divinelink.core.ui.rating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_ic_rt
import com.divinelink.core.model.resources.core_model_ic_rt_bad_rating
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_percentage
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RottenTomatoesRatingItem(
  modifier: Modifier = Modifier,
  ratingDetails: RatingDetails,
) {
  Row(
    modifier = modifier.testTag(TestTags.Rating.RT_RATING),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    AnimatedVisibility(visible = ratingDetails is RatingDetails.Rating) {
      val image = if ((ratingDetails as RatingDetails.Rating).value in 0..59) {
        painterResource(Res.drawable.core_model_ic_rt_bad_rating)
      } else {
        painterResource(Res.drawable.core_model_ic_rt)
      }
      Image(
        modifier = Modifier.size(MaterialTheme.dimensions.keyline_32),
        painter = image,
        contentDescription = null,
      )
    }

    when (ratingDetails) {
      RatingDetails.Initial -> RottenTomatoesRatingContentShimmer()
      RatingDetails.Unavailable -> Text(
        text = "-",
        style = MaterialTheme.typography.labelLarge,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
      )
      is RatingDetails.Score -> Unit
      is RatingDetails.Rating -> Text(
        text = stringResource(
          UiString.core_ui_percentage,
          ratingDetails.value,
        ),
        style = MaterialTheme.typography.labelLarge,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
      )
    }
  }
}

@Composable
private fun RottenTomatoesRatingContentShimmer(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.testTag(TestTags.Rating.RT_RATING_SKELETON),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = " ",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .width(MaterialTheme.dimensions.keyline_72)
        .height(MaterialTheme.dimensions.keyline_40)
        .shimmer()
        .clip(MaterialTheme.shapes.extraLarge)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  }
}

@Previews
@Composable
fun RottenTomatoesRatingItemPreview() {
  AppTheme {
    Surface {
      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        RottenTomatoesRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(77),
        )
        RottenTomatoesRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Rating(59),
        )
        RottenTomatoesRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Initial,
        )
        RottenTomatoesRatingItem(
          modifier = Modifier,
          ratingDetails = RatingDetails.Unavailable,
        )
      }
    }
  }
}
