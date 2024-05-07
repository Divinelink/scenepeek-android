package com.andreolas.movierama.details.ui.rate

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions

@Composable
@Suppress("MagicNumber")
fun RateSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
) {
  val rating = remember {
    mutableFloatStateOf(value)
  }

  val color = animateColorAsState(
    targetValue = when (rating.floatValue) {
      in 0.1f..3.5f -> Color(0xFFDB2360)
      in 3.5f..6.9f -> Color(0xFFD2D531)
      in 7.0f..10.0f -> Color(0xFF21D07A)
      else -> Color.White
    },
    label = "Color Rating Slider"
  )

  Slider(
    modifier = Modifier.fillMaxWidth(),
    colors = SliderDefaults.colors(
      thumbColor = color.value,
      activeTrackColor = color.value,
      inactiveTrackColor = color.value.copy(alpha = 0.2f),
    ),
    value = rating.floatValue,
    steps = 8,
    valueRange = 1.0f..10f,
    onValueChange = { newValue ->
      onValueChange(newValue)
      rating.floatValue = newValue
    }
  )
}

@Preview(
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun RateSliderPreview() {
  AppTheme {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
      RateSlider(
        value = 0f,
        onValueChange = {}
      )

      RateSlider(
        value = 1f,
        onValueChange = {}
      )

      RateSlider(
        value = 5f,
        onValueChange = {}
      )

      RateSlider(
        value = 8f,
        onValueChange = {}
      )
    }
  }
}
