package com.andreolas.movierama.ui.components.details.reviews

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.PopularMovieItemShape
import com.divinelink.core.model.details.Review
import com.divinelink.ui.MediaRatingItem

private const val MINIMUM_MAX_LINES = 6
private val MINIMUM_CARD_SIZE = 260.dp
private val MAXIMUM_CARD_SIZE = 360.dp

@Composable
fun ReviewItemCard(
  modifier: Modifier = Modifier,
  review: Review,
) {
  var expanded by remember { mutableStateOf(false) }
  var maxLines by remember { mutableIntStateOf(MINIMUM_MAX_LINES) }
  var maxWidth by remember { mutableStateOf(MINIMUM_CARD_SIZE) }

  Card(
    shape = PopularMovieItemShape,
    modifier = Modifier
      .clip(PopularMovieItemShape)
      .animateContentSize(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioLowBouncy,
          stiffness = Spring.StiffnessLow,
        )
      )
      .widthIn(max = maxWidth)
      .clipToBounds()
      .clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
      ) {
        expanded = !expanded
        maxLines = if (!expanded) MINIMUM_MAX_LINES else Int.MAX_VALUE
        maxWidth = if (!expanded) MINIMUM_CARD_SIZE else MAXIMUM_CARD_SIZE
      },
  ) {
    review.rating?.let { rating ->
      Row(
        horizontalArrangement = Arrangement.Center,
      ) {
        MediaRatingItem(
          modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
          rating = rating.toString(),
        )
      }
    }

    Text(
      modifier = Modifier
        .padding(top = 16.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
        .animateContentSize(
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
          )
        ),
      text = review.content,
      style = MaterialTheme.typography.bodyLarge,
      overflow = TextOverflow.Ellipsis,
      maxLines = maxLines,
    )

    Text(
      modifier = Modifier
        .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 12.dp),
      text = "A review by: ${review.authorName}, ${review.date}",
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
      style = MaterialTheme.typography.bodySmall,
      fontStyle = FontStyle.Italic,
    )
  }
}

@ExcludeFromKoverReport
@Preview
@Composable
@Preview(
  name = "Night Mode",
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
  name = "Day Mode",
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Suppress("Magic Number")
fun ReviewItemCardPreview() {
  AppTheme {
    val review = Review(
      authorName = "Author Lorem",
      rating = 10,
      content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales " +
        "laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu" +
        "elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat" +
        "risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante" +
        "nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam" +
        "neque. Praesent ut lacus sed justo viverra fermentum et ut sem. \n Fusce" +
        "convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia." +
        "Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum" +
        "ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis" +
        "purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.",
      date = "2022-10-22"
    )
    Surface {
      ReviewItemCard(
        review = review,
      )
    }
  }
}
