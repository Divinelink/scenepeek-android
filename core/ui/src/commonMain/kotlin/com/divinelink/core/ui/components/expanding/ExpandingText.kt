package com.divinelink.core.ui.components.expanding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_read_more
import org.jetbrains.compose.resources.stringResource

private const val MINIMIZED_MAX_LINES = 6

@Composable
fun ExpandingText(
  modifier: Modifier = Modifier,
  text: String,
  style: TextStyle,
  expandComponent: @Composable BoxScope.(Modifier) -> Unit = {},
  shrinkComponent: @Composable ColumnScope.(Modifier, () -> Unit) -> Unit = { _, _ -> },
) {
  val showMore = remember { mutableStateOf(false) }
  val hasOverflow = remember { mutableStateOf(false) }

  Column(
    modifier = modifier.clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = null,
      enabled = hasOverflow.value,
    ) { showMore.value = true },
  ) {
    AnimatedContent(
      targetState = showMore.value,
      label = "Text resizing animation",
      transitionSpec = { (fadeIn(initialAlpha = 1f).togetherWith(fadeOut(targetAlpha = 1f))) },
    ) { showExpandedText ->
      if (showExpandedText) {
        Column {
          Text(text = text, style = style)
          shrinkComponent(Modifier) { showMore.value = false }
        }
      } else {
        Box {
          Text(
            text = text,
            maxLines = MINIMIZED_MAX_LINES,
            onTextLayout = { textLayoutResult ->
              if (textLayoutResult.hasVisualOverflow) {
                hasOverflow.value = true
              }
            },
            overflow = TextOverflow.Clip,
            style = style,
          )

          if (hasOverflow.value) {
            expandComponent(Modifier.align(Alignment.BottomEnd))
          }
        }
      }
    }
  }
}

@Composable
@Previews
private fun ExpandingTextPreview() {
  AppTheme {
    Surface {
      Column {
        ExpandingText(
//          text = LoremIpsum(50).values.joinToString(),
          text = "LoremIpsum(50).values.joinToString()",
          style = MaterialTheme.typography.bodyMedium,
          expandComponent = { modifier ->
            ExpandingComponents.InlineEdgeFadingEffect(
              modifier = modifier,
              text = stringResource(UiString.core_ui_read_more),
            )
          },
          shrinkComponent = { modifier, onClick ->
            ExpandingComponents.ShowLess(modifier = modifier, onClick = onClick)
          },
        )
      }
    }
  }
}

@Composable
@Previews
private fun ExpandingTextNoOverflowPreview() {
  AppTheme {
    Surface {
      Column {
        ExpandingText(
//          text = LoremIpsum(20).values.joinToString(),
          text = "LoremIpsum(20).values.joinToString()",
          style = MaterialTheme.typography.bodyMedium,
          expandComponent = { modifier ->
            ExpandingComponents.InlineEdgeFadingEffect(
              modifier = modifier,
              text = stringResource(UiString.core_ui_read_more),
            )
          },
          shrinkComponent = { modifier, onClick ->
            ExpandingComponents.ShowLess(modifier = modifier, onClick = onClick)
          },
        )
      }
    }
  }
}
