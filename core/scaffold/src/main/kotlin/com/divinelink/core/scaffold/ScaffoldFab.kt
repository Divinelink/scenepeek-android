package com.divinelink.core.scaffold

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.UiTokens
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun ScaffoldState.ScaffoldFab(
  modifier: Modifier = Modifier,
  enterTransition: EnterTransition = slideInVertically(initialOffsetY = { it }),
  exitTransition: ExitTransition = slideOutVertically(targetOffsetY = { it }),
  text: String?,
  icon: ImageVector?,
  expanded: Boolean,
  onClick: () -> Unit,
) {
  AnimatedVisibility(
    visible = canShowFab,
    enter = enterTransition,
    exit = exitTransition,
    content = {
      FloatingActionButton(
        modifier = modifier
          .sharedElement(
            sharedContentState = rememberSharedContentState(
              FabSharedElementKey,
            ),
            animatedVisibilityScope = this@ScaffoldFab,
          ),
        onClick = onClick,
        content = {
          Row(
            modifier = Modifier
              .padding(horizontal = MaterialTheme.dimensions.keyline_16),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            if (icon != null) FabIcon(icon)
            if (icon == null || expanded) {
              if (icon != null) {
                Spacer(modifier = Modifier.width(MaterialTheme.dimensions.keyline_8))
              }
              text?.let {
                AnimatedContent(targetState = text) { text ->
                  Text(
                    text = text,
                    maxLines = 1,
                  )
                }
              }
            }
          }
        },
      )
    },
  )
}

@Composable
private fun FabIcon(icon: ImageVector) {
  Icon(
    modifier = Modifier,
    imageVector = icon,
    contentDescription = null,
  )
}

fun ScaffoldState.isFabExpanded(offset: Offset): Boolean = offset.y < with(density) { 56.dp.toPx() }

fun ScaffoldState.fabOffset(offset: Offset): IntOffset = if (isMediumScreenWidthOrWider.value) {
  IntOffset.Zero
} else {
  IntOffset(
    x = offset.x.roundToInt(),
    y = min(
      offset.y.roundToInt(),
      with(density) { UiTokens.bottomNavHeight.roundToPx() },
    ),
  )
}

private data object FabSharedElementKey
