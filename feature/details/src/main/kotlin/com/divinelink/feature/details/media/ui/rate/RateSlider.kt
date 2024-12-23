package com.divinelink.feature.details.media.ui.rate

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags

@Composable
@Suppress("MagicNumber")
fun RateSlider(
  modifier: Modifier = Modifier,
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
      else -> MaterialTheme.colorScheme.onSurface
    },
    label = "Color Rating Slider",
  )

  Slider(
    modifier = modifier
      .testTag(TestTags.Details.RATE_SLIDER)
      .fillMaxWidth(),
    colors = SliderDefaults.colors(
      thumbColor = color.value,
      activeTrackColor = color.value,
      inactiveTrackColor = color.value.copy(alpha = 0.2f),
      inactiveTickColor = color.value.copy(alpha = 0.2f),
    ),
    value = rating.floatValue,
    steps = 8,
    valueRange = 1.0f..10f,
    onValueChange = { newValue ->
      onValueChange(newValue)
      rating.floatValue = newValue
    },
  )
}

@Previews
@Composable
private fun RateSliderPreview() {
  AppTheme {
    Surface {
      Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8)) {
        RateSlider(
          value = 0f,
          onValueChange = {},
        )

        RateSlider(
          value = 1f,
          onValueChange = {},
        )

        RateSlider(
          value = 5f,
          onValueChange = {},
        )

        RateSlider(
          value = 8f,
          onValueChange = {},
        )
      }
    }
  }
}
