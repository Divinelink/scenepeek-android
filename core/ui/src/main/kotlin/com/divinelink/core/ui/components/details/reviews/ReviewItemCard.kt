package com.divinelink.core.ui.components.details.reviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import com.divinelink.core.commons.extensions.markdownToHtml
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.details.review.ReviewFactory
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.coil.AvatarImage
import com.divinelink.core.ui.extension.getColorRating
import com.divinelink.core.ui.text.SimpleExpandingText

@Composable
fun ReviewItemCard(
  modifier: Modifier = Modifier,
  review: Review,
) {
  Card(
    modifier = modifier
      .testTag(TestTags.Details.Reviews.REVIEW_CARD.format(review.content))
      .fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      AvatarImage.Small(
        avatarUrl = AvatarImage.buildTMDBAvatarUrl(review.author.avatarPath),
        username = review.author.username,
      )

      Column {
        Text(
          text = review.author.displayName,
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
        )

        review.date?.let {
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
          )
        }
      }
    }
    SimpleExpandingText(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
      text = AnnotatedString.fromHtml(review.content.markdownToHtml()),
    )

    review.rating?.let {
      OutlinedCard(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_16),
        colors = CardDefaults.cardColors(),
        border = BorderStroke(1.dp, color = it.toDouble().getColorRating()),
      ) {
        Row(
          modifier = Modifier
            .padding(
              horizontal = MaterialTheme.dimensions.keyline_12,
              vertical = MaterialTheme.dimensions.keyline_6,
            ),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
          )

          Text(
            text = it.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
          )
        }
      }
      Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_16))
    }
  }
}

@Composable
@Previews
fun ReviewItemCardPreview() {
  AppTheme {
    Surface {
      ReviewItemCard(review = ReviewFactory.full())
    }
  }
}
