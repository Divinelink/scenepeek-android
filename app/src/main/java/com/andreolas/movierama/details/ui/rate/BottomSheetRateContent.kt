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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions

@Composable
fun BottomSheetRateContent(
  value: Float,
  onRateChanged: (Float) -> Unit,
  mediaTitle: String,
  onSubmitRate: (Float) -> Unit = {},
) {
  Column(
    modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "What did you think of $mediaTitle?"
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
      onValueChange = onRateChanged
    )

    TextButton(
      modifier = Modifier.align(Alignment.End),
      onClick = {
        // Submit
      }) {
      Text(text = "Clear my rating")
    }

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = "Total score: ${value.toInt()}",
    )

    Button(
      modifier = Modifier.fillMaxWidth(),
      onClick = {
        // Submit
      }) {
      Text(
        text = "Submit rating",
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
      BottomSheetRateContent(
        value = 5f,
        onRateChanged = {},
        mediaTitle = "The Godfather",
      )
    }
  }
}
