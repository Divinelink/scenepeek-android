package com.divinelink.feature.add.to.account.rate

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.rating_label_1
import com.divinelink.feature.add.to.account.resources.rating_label_10
import com.divinelink.feature.add.to.account.resources.rating_label_2
import com.divinelink.feature.add.to.account.resources.rating_label_3
import com.divinelink.feature.add.to.account.resources.rating_label_4
import com.divinelink.feature.add.to.account.resources.rating_label_5
import com.divinelink.feature.add.to.account.resources.rating_label_6
import com.divinelink.feature.add.to.account.resources.rating_label_7
import com.divinelink.feature.add.to.account.resources.rating_label_8
import com.divinelink.feature.add.to.account.resources.rating_label_9
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

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

  RateSliderWithTooltip(
    modifier = modifier
      .testTag(TestTags.Details.RATE_SLIDER)
      .fillMaxWidth(),
    rating = rating,
    color = color,
    onValueChange = onValueChange,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateSliderWithTooltip(
  rating: MutableFloatState,
  color: State<Color>,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  thumbRadius: Dp = MaterialTheme.dimensions.keyline_10,
  lingerDurationMillis: Long = 3_000L,
) {
  val valueRange = 1f..10f
  val thumbRadiusPx = with(LocalDensity.current) { thumbRadius.toPx() }
  var sliderSize by remember { mutableStateOf(IntSize.Zero) }

  val fraction = ((rating.floatValue - valueRange.start) /
    (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
  val trackWidthPx = (sliderSize.width - 2 * thumbRadiusPx).coerceAtLeast(0f)
  val thumbCenterX = thumbRadiusPx + fraction * trackWidthPx
  val thumbTopY = (sliderSize.height / 2f - thumbRadiusPx).coerceAtLeast(0f)

  val tooltipState = rememberTooltipState(isPersistent = true)
  var activity by remember { mutableIntStateOf(0) }
  val scope = rememberCoroutineScope()

  LaunchedEffect(activity) {
    if (activity != 0) {
      scope.launch {
        delay(150)
        tooltipState.show()
      }
    }
    delay(lingerDurationMillis)
    tooltipState.dismiss()
  }

  Box(modifier = modifier.fillMaxWidth()) {
    Slider(
      modifier = Modifier
        .testTag(TestTags.Details.RATE_SLIDER)
        .fillMaxWidth()
        .onSizeChanged { sliderSize = it },
      colors = SliderDefaults.colors(
        thumbColor = color.value,
        activeTrackColor = color.value,
        inactiveTrackColor = color.value.copy(alpha = 0.2f),
        inactiveTickColor = color.value.copy(alpha = 0.2f),
      ),
      value = rating.floatValue,
      steps = 8,
      valueRange = valueRange,
      onValueChange = { newValue ->
        onValueChange(newValue)
        rating.floatValue = newValue
        activity++
      },
    )

    Box(
      modifier = Modifier.offset {
        IntOffset(thumbCenterX.roundToInt(), thumbTopY.roundToInt())
      },
    ) {
      TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
          TooltipAnchorPosition.Above,
        ),
        state = tooltipState,
        focusable = false,
        enableUserInput = false,
        tooltip = {
          ratingLabelFor(rating.floatValue)?.let { label ->
            PlainTooltip {
              Text(label)
            }
          }
        },
        content = {
          Spacer(
            modifier = Modifier.size(MaterialTheme.dimensions.keyline_1),
          )
        },
      )
    }
  }
}

@Composable
private fun ratingLabelFor(rating: Float): String? {
  val index = (rating.roundToInt()).coerceIn(1, 10)

  return when (index) {
    1 -> Res.string.rating_label_1
    2 -> Res.string.rating_label_2
    3 -> Res.string.rating_label_3
    4 -> Res.string.rating_label_4
    5 -> Res.string.rating_label_5
    6 -> Res.string.rating_label_6
    7 -> Res.string.rating_label_7
    8 -> Res.string.rating_label_8
    9 -> Res.string.rating_label_9
    10 -> Res.string.rating_label_10
    else -> null
  }?.let { resource -> stringResource(resource) }
}

@Previews
@Composable
private fun RateSliderPreview() {
  PreviewLocalProvider {
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
