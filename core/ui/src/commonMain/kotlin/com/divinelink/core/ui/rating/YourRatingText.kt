package com.divinelink.core.ui.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_your_rating
import com.divinelink.core.ui.extension.format
import com.divinelink.core.ui.extension.getColorRating
import org.jetbrains.compose.resources.stringResource

@Composable
fun YourRatingText(
  modifier: Modifier = Modifier,
  accountRating: Int,
) {
  val color = accountRating.toDouble().getColorRating()

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(UiString.core_ui_your_rating),
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.titleSmall,
      textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))

    Text(
      modifier = Modifier.testTag(TestTags.Details.YOUR_RATING.format(accountRating)),
      text = accountRating.toString(),
      color = color,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
    )
  }
}
