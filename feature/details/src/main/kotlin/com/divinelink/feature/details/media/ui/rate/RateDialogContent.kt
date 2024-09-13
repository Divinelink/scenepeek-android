package com.divinelink.feature.details.media.ui.rate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.components.details.SpannableRating
import com.divinelink.feature.details.R
import kotlin.math.roundToInt

@Composable
fun RateDialogContent(
  modifier: Modifier = Modifier,
  value: Float,
  onRateChanged: (Float) -> Unit,
  mediaTitle: String,
  canClearRate: Boolean,
  onSubmitRate: (Int) -> Unit,
  onClearRate: () -> Unit,
) {
  val rating = remember { mutableFloatStateOf(value) }

  Column(
    modifier = modifier.padding(MaterialTheme.dimensions.keyline_16),
  ) {
    Text(
      text = stringResource(id = R.string.details__add_rating_description, mediaTitle),
    )

    Spacer(
      modifier = Modifier.height(MaterialTheme.dimensions.keyline_32),
    )

    SpannableRating(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(id = R.string.details__your_rating),
      rating = " ${rating.floatValue.roundToInt()}",
    )

    RateSlider(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      value = value,
      onValueChange = {
        onRateChanged(it)
        rating.floatValue = it
      },
    )

    if (canClearRate) {
      TextButton(
        modifier = Modifier
          .padding(bottom = MaterialTheme.dimensions.keyline_16)
          .align(Alignment.End),
        onClick = onClearRate,
      ) {
        Text(text = stringResource(id = R.string.details__clear_my_rating))
      }
    }

    Button(
      modifier = Modifier.fillMaxWidth(),
      enabled = rating.floatValue > 0,
      onClick = { onSubmitRate(rating.floatValue.roundToInt()) },
    ) {
      Text(
        text = stringResource(id = R.string.details__submit_rating_button),
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
        onRateChanged = {},
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
        value = 5f,
        mediaTitle = "The Godfather",
        onRateChanged = {},
        onSubmitRate = {},
        onClearRate = {},
        canClearRate = false,
      )
    }
  }
}
