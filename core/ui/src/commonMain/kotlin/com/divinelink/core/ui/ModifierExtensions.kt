package com.divinelink.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape

/**
 * Conditionally applies the given [Modifier]s based on the given [condition].
 *
 * @param condition The condition to check.
 * @param ifTrue The [Modifier] to apply if the [condition] is `true`.
 * @param ifFalse The [Modifier] to apply if the [condition] is `false`.
 */
inline fun Modifier.conditional(
  condition: Boolean,
  ifTrue: Modifier.() -> Modifier = { this },
  ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (condition) {
  then(ifTrue(Modifier))
} else {
  then(ifFalse(Modifier))
}

fun Modifier.blurEffect(
  radiusX: Float = 15f,
  radiusY: Float = 15f,
): Modifier = this.then(
  Modifier.graphicsLayer {
    renderEffect = BlurEffect(
      radiusX = radiusX,
      radiusY = radiusY,
      edgeTreatment = TileMode.Clamp,
    )
  },
)

@Composable
fun Modifier.mediaImageDropShadow() = this.then(
  Modifier.dropShadow(
    shape = MaterialTheme.shape.large,
    shadow = Shadow(
      radius = MaterialTheme.dimensions.keyline_16,
      spread = MaterialTheme.dimensions.keyline_2,
      alpha = 0.6f,
    ),
  ),
)
