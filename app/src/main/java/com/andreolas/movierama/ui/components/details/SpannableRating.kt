package com.andreolas.movierama.ui.components.details

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.getColorRating

@Composable
fun SpannableRating(
  modifier: Modifier = Modifier,
  text: String,
  rating: String,
  newLine: Boolean = false
) {
  val color = rating.getColorRating()

  val annotatedString = buildAnnotatedString {
    append(text)
    withStyle(
      style = SpanStyle(
        color = color,
        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
      )
    ) {
      if (newLine) {
        append("\n")
      }
      append(rating)
    }
  }

  Text(
    modifier = modifier.testTag(TestTags.Details.YOUR_RATING),
    text = annotatedString,
    lineHeight = MaterialTheme.typography.headlineSmall.lineHeight,
    color = MaterialTheme.colorScheme.onSurface,
    style = MaterialTheme.typography.titleMedium,
    textAlign = TextAlign.Center,
  )
}
