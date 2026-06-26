package com.divinelink.core.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.composition.PreviewLocalProvider

@Composable
fun Modifier.animatedSpreadShadow(
  shape: Shape,
  colors: List<Color> = listOf(
    MaterialTheme.colors.redHighlight,
    MaterialTheme.colors.crimsonRed,
    MaterialTheme.colors.vibrantPurple,
    MaterialTheme.colors.brightOrange,
    MaterialTheme.colors.limeYellow,
    MaterialTheme.colors.emeraldGreen,
    MaterialTheme.colors.gray,
    MaterialTheme.colors.redHighlight,
  ),
  minSpread: Dp = MaterialTheme.dimensions.keyline_2,
  maxSpread: Dp = MaterialTheme.dimensions.keyline_14,
  radius: Dp = MaterialTheme.dimensions.keyline_12,
  offset: DpOffset = DpOffset.Zero,
  alpha: Float = 1f,
  durationMillis: Int = 1200,
): Modifier {
  val transition = rememberInfiniteTransition(label = "animated-spread-shadow")
  val spread by transition.animateFloat(
    initialValue = minSpread.value,
    targetValue = maxSpread.value,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = durationMillis, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse,
    ),
    label = "spread",
  )
  return this.dropShadow(
    shape = shape,
    shadow = Shadow(
      radius = radius,
      spread = spread.dp,
      brush = Brush.sweepGradient(colors),
      offset = offset,
      alpha = alpha,
    ),
  )
}

@Previews
@Composable
private fun AnimatedSpreadShadowPreview() {
  PreviewLocalProvider {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(MaterialTheme.dimensions.keyline_48),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_48),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Box(
        modifier = Modifier
          .animatedSpreadShadow(
            shape = MaterialTheme.shape.medium,
            maxSpread = MaterialTheme.dimensions.keyline_6,
            minSpread = MaterialTheme.dimensions.keyline_0,
          )
          .background(color = Color(0xFF1B5E20), shape = MaterialTheme.shape.medium)
          .padding(
            horizontal = MaterialTheme.dimensions.keyline_24,
            vertical = MaterialTheme.dimensions.keyline_12,
          ),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = "Button",
          color = Color.White,
          fontWeight = FontWeight.SemiBold,
          fontSize = 16.sp,
        )
      }
    }
  }
}
