package com.divinelink.core.ui.components.details.reviews

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.commons.extensions.markdownToHtml
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.PopularMovieItemShape
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Review
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.rating.TMDBRatingItem

private const val MINIMUM_MAX_LINES = 6
private val MINIMUM_CARD_SIZE = 260.dp
private val MAXIMUM_CARD_SIZE = 360.dp

@Composable
fun ReviewItemCard(
  modifier: Modifier = Modifier,
  review: Review,
) {
  val expanded = remember { mutableStateOf(false) }
  val maxLines = remember { mutableIntStateOf(MINIMUM_MAX_LINES) }
  val maxWidth = remember { mutableStateOf(MINIMUM_CARD_SIZE) }

  Card(
    shape = PopularMovieItemShape,
    modifier = Modifier
      .clip(PopularMovieItemShape)
      .animateContentSize(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioLowBouncy,
          stiffness = Spring.StiffnessLow,
        ),
      )
      .widthIn(max = maxWidth.value)
      .clipToBounds()
      .clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
      ) {
        expanded.value = !expanded.value
        maxLines.intValue = if (!expanded.value) MINIMUM_MAX_LINES else Int.MAX_VALUE
        maxWidth.value = if (!expanded.value) MINIMUM_CARD_SIZE else MAXIMUM_CARD_SIZE
      },
  ) {
    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_12))
    review.rating?.let { rating ->
      TMDBRatingItem(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_12),
        rating = rating.toDouble(),
        voteCount = null,
      )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.dimensions.keyline_12))

    Text(
      modifier = Modifier
        .padding(horizontal = MaterialTheme.dimensions.keyline_12)
        .animateContentSize(
          animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
          ),
        ),
      text = AnnotatedString.fromHtml(review.content.markdownToHtml()),
      style = MaterialTheme.typography.bodyLarge,
      overflow = TextOverflow.Ellipsis,
      maxLines = maxLines.intValue,
    )

    Text(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_12),
      text = stringResource(
        R.string.core_ui_review_author,
        review.authorName,
        review.date ?: "",
      ),
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
      style = MaterialTheme.typography.bodySmall,
      fontStyle = FontStyle.Italic,
    )
  }
}

@Composable
@Previews
@Suppress("Magic Number")
private fun ReviewItemCardPreview() {
  AppTheme {
    val review = Review(
      authorName = "Author Lorem",
      rating = 10,
      content = "**Lorem ipsum dolor sit amet**, *consectetur adipiscing elit*." +
        "\r\n\r\nInteger sodales " +
        "laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu" +
        "elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat" +
        "risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante" +
        "nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam" +
        "neque. Praesent ut lacus sed justo viverra fermentum et ut sem. \n Fusce" +
        "convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia." +
        "Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum" +
        "ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis" +
        "purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.",
      date = "2022-10-22",
    )
    Surface {
      ReviewItemCard(
        review = review,
      )
    }
  }
}
