package com.divinelink.feature.details.media.ui.rate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.details.SpannableRating
import com.divinelink.core.ui.fromHtml
import com.divinelink.core.ui.resources.core_ui_your_rating
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.details__add_rating_description
import com.divinelink.feature.details.resources.details__clear_my_rating
import com.divinelink.feature.details.resources.details__submit_rating_button
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun RateDialogContent(
  modifier: Modifier = Modifier,
  value: Float,
  mediaTitle: String,
  canClearRate: Boolean,
  onSubmitRate: (Int) -> Unit,
  onClearRate: () -> Unit,
) {
  var rating by remember { mutableFloatStateOf(value) }
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .verticalScroll(scrollState)
      .padding(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(Res.string.details__add_rating_description, mediaTitle).fromHtml(),
    )

    Spacer(
      modifier = Modifier.height(MaterialTheme.dimensions.keyline_32),
    )

    SpannableRating(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.core_ui_your_rating),
      rating = rating.roundToInt(),
    )

    RateSlider(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      value = value,
      onValueChange = {
        rating = it
      },
    )

    if (canClearRate) {
      TextButton(
        modifier = Modifier
          .padding(bottom = MaterialTheme.dimensions.keyline_16)
          .align(Alignment.End),
        onClick = onClearRate,
      ) {
        Text(text = stringResource(Res.string.details__clear_my_rating))
      }
    }

    Button(
      modifier = Modifier.fillMaxWidth(),
      enabled = rating > 0,
      onClick = { onSubmitRate(rating.roundToInt()) },
    ) {
      Text(
        text = stringResource(Res.string.details__submit_rating_button),
      )
    }
  }
}

@Previews
@Composable
private fun BottomSheetRateContentPreview() {
  AppTheme {
    Surface {
      RateDialogContent(
        value = 5f,
        mediaTitle = "The Godfather",
        onSubmitRate = {},
        onClearRate = {},
        canClearRate = true,
      )
    }
  }
}

@Previews
@Composable
private fun BottomSheetRateContentWithoutClearPreview() {
  AppTheme {
    Surface {
      RateDialogContent(
        value = 0f,
        mediaTitle = "The Godfather",
        onSubmitRate = {},
        onClearRate = {},
        canClearRate = false,
      )
    }
  }
}
