package com.andreolas.movierama.details.ui.rate

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions
import kotlin.math.roundToInt

@Composable
fun RateBottomSheetContent(
  modifier: Modifier = Modifier,
  value: Float,
  onRateChanged: (Float) -> Unit,
  mediaTitle: String,
  onSubmitRate: (Int) -> Unit = {},
) {
  val rating = remember {
    mutableStateOf(value)
  }

  Column(
    modifier = modifier.padding(MaterialTheme.dimensions.keyline_16)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = stringResource(id = R.string.details__add_rating_description, mediaTitle),
      )

      Spacer(
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(
      modifier = Modifier.height(MaterialTheme.dimensions.keyline_48)
    )

    RateSlider(
      value = value,
      onValueChange = {
        onRateChanged(it)
        rating.value = it
      }
    )

    TextButton(
      modifier = Modifier.align(Alignment.End),
      onClick = {
        // Clear rating
      }) {
      Text(text = stringResource(id = R.string.details__clear_my_rating))
    }

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(id = R.string.details__your_score, rating.value.roundToInt()),
      style = MaterialTheme.typography.titleMedium,
    )

    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = { onSubmitRate(rating.value.roundToInt()) }) {
      Text(
        text = stringResource(id = R.string.details__submit_rating_button),
        style = MaterialTheme.typography.titleMedium,
      )
    }
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomSheetRateContentPreview() {
  AppTheme {
    Surface {
      RateBottomSheetContent(
        value = 5f,
        onRateChanged = {},
        mediaTitle = "The Godfather",
      )
    }
  }
}
