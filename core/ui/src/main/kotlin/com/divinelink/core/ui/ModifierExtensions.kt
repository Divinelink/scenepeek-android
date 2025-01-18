package com.divinelink.core.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree

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
  graphicsLayer {
    renderEffect = BlurEffect(
      radiusX = radiusX,
      radiusY = radiusY,
      edgeTreatment = TileMode.Clamp,
    )
  },
)

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
  autofillTypes: List<AutofillType>,
  onFill: ((String) -> Unit),
) = composed {
  val autofill = LocalAutofill.current
  val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
  LocalAutofillTree.current += autofillNode

  this
    .onGloballyPositioned {
      autofillNode.boundingBox = it.boundsInWindow()
    }
    .onFocusChanged { focusState ->
      autofill?.run {
        if (focusState.isFocused) {
          requestAutofillForNode(autofillNode)
        } else {
          cancelAutofillForNode(autofillNode)
        }
      }
    }
}
