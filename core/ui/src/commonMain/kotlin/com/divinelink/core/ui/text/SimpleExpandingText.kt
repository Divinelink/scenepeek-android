package com.divinelink.core.ui.text

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.core_ui_show_less
import com.divinelink.core.ui.core_ui_show_more
import org.jetbrains.compose.resources.stringResource

private const val MINIMUM_MAX_LINES = 6

@Composable
fun SimpleExpandingText(
  modifier: Modifier = Modifier,
  text: AnnotatedString,
) {
  var isExpanded by remember { mutableStateOf(false) }
  var hasOverflow by remember { mutableStateOf(false) }
  var maxLines by remember { mutableIntStateOf(MINIMUM_MAX_LINES) }

  Column(
    modifier = modifier
      .conditional(
        condition = hasOverflow,
        ifFalse = {
          padding(bottom = MaterialTheme.dimensions.keyline_16)
        },
      )
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
          if (hasOverflow) {
            isExpanded = !isExpanded
            maxLines = if (!isExpanded) MINIMUM_MAX_LINES else Int.MAX_VALUE
          }
        },
      ),
  ) {
    Column {
      Text(
        modifier = Modifier
          .animateContentSize(
            animationSpec = spring(
              dampingRatio = Spring.DampingRatioLowBouncy,
              stiffness = Spring.StiffnessLow,
            ),
          ),
        onTextLayout = { textLayoutResult ->
          if (textLayoutResult.hasVisualOverflow) {
            hasOverflow = true
          }
        },
        overflow = TextOverflow.Ellipsis,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = maxLines,
      )
      if (hasOverflow) {
        TextButton(
          modifier = Modifier.offset(x = -MaterialTheme.dimensions.keyline_8),
          onClick = {
            isExpanded = !isExpanded
            maxLines = if (!isExpanded) MINIMUM_MAX_LINES else Int.MAX_VALUE
          },
        ) {
          Text(
            text = if (isExpanded) {
              stringResource(UiString.core_ui_show_less)
            } else {
              stringResource(UiString.core_ui_show_more)
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
          )
        }
      }
    }
  }
}

@Composable
@Previews
fun SimpleExpandingTextPreview() {
  AppTheme {
    Surface {
      SimpleExpandingText(
        modifier = Modifier.padding(top = 120.dp),
        text = buildAnnotatedString {
          append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
          append("Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ")
          append(
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
              " aliquip ex ea commodo consequat. ",
          )
          append(
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
              "dolore eu fugiat nulla pariatur. ",
          )
        },
      )
    }
  }
}
