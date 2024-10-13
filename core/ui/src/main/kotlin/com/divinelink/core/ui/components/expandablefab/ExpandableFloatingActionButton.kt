package com.divinelink.core.ui.components.expandablefab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.snackbar.controller.LocalSnackbarController
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController

@Composable
fun ExpandableFloatActionButton(
  modifier: Modifier = Modifier,
  buttons: List<FloatingActionButtonItem>,
) {
  val snackbarController = LocalSnackbarController.current

  var expanded by remember { mutableStateOf(false) }
  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 45f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "Floating Action Button Rotation Animation",
  )

  val fabOffset by animateDpAsState(
    targetValue = if (snackbarController.isVisible()) {
      MaterialTheme.dimensions.keyline_58
    } else {
      MaterialTheme.dimensions.keyline_0
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow,
    ),
    label = "Snackbar Offset Animation",
  )

  Box(modifier = modifier.fillMaxSize()) {
    AnimatedVisibility(
      modifier = Modifier
        .fillMaxSize()
        .offset(
          x = MaterialTheme.dimensions.keyline_16,
          y = MaterialTheme.dimensions.keyline_16,
        ),
      visible = expanded,
      enter = fadeIn(animationSpec = tween(durationMillis = 300)),
      exit = fadeOut(animationSpec = tween(durationMillis = 300)),
    ) {
      Box(
        modifier = Modifier
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
          .testTag(TestTags.Components.ExpandableFab.BACKGROUND)
          .fillMaxSize()
          .clickable(
            interactionSource = null,
            indication = null,
            onClick = { expanded = false },
          ),
      )
    }

    Column(
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.Bottom,
      modifier = Modifier
        .offset { IntOffset(0, -fabOffset.roundToPx()) }
        .fillMaxSize(),
    ) {
      StaggeredFloatingActionsButtons(
        fabItems = buttons,
        expanded = expanded,
        onDismiss = { expanded = false },
      )

      Spacer(modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8))

      FloatingActionButton(
        onClick = { expanded = !expanded },
        modifier = Modifier
          .testTag(TestTags.Components.ExpandableFab.BUTTON)
          .rotate(rotationState),
      ) {
        Icon(
          imageVector = Icons.Rounded.Add,
          contentDescription = "Expandable FAB",
        )
      }
    }
  }
}

@Composable
private fun StaggeredFloatingActionsButtons(
  fabItems: List<FloatingActionButtonItem>,
  expanded: Boolean,
  onDismiss: () -> Unit,
) {
  fabItems.forEachIndexed { index, item ->
    StaggeredAnimatedFloatingActionButton(
      expanded = expanded,
      index = index,
      fabItem = item,
      onDismiss = onDismiss,
    )
  }
}

@Composable
fun StaggeredAnimatedFloatingActionButton(
  expanded: Boolean,
  fabItem: FloatingActionButtonItem,
  index: Int,
  onDismiss: () -> Unit,
) {
  val enterTransition = remember {
    fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = index * 50)) +
      slideInVertically(
        animationSpec = tween(
          durationMillis = 200,
          delayMillis = index * 50,
        ),
      ) { fullHeight -> fullHeight }
  }
  val exitTransition = remember {
    fadeOut(animationSpec = tween(durationMillis = 200)) +
      slideOutVertically(animationSpec = tween(durationMillis = 200)) { fullHeight -> fullHeight }
  }

  AnimatedVisibility(
    visible = expanded,
    enter = enterTransition,
    exit = exitTransition,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      modifier = Modifier
        .padding(bottom = MaterialTheme.dimensions.keyline_16)
        .clickable(
          interactionSource = null,
          indication = null,
          onClick = {
            fabItem.onClick()
            onDismiss()
          },
        ),
    ) {
      Text(
        text = fabItem.label.getString(),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8),
      )
      SmallFloatingActionButton(
        onClick = {
          fabItem.onClick()
          onDismiss()
        },
      ) {
        when (fabItem.icon) {
          is IconWrapper.Image -> Image(
            painter = painterResource(id = fabItem.icon.resourceId),
            contentDescription = fabItem.contentDescription.getString(),
          )
          is IconWrapper.Icon -> Icon(
            painter = painterResource(id = fabItem.icon.resourceId),
            contentDescription = fabItem.contentDescription.getString(),
          )
          is IconWrapper.Vector -> Icon(
            imageVector = fabItem.icon.vector,
            contentDescription = fabItem.contentDescription.getString(),
          )
        }
      }
    }
  }
}

@Previews
@Composable
private fun ExpandableFloatingActionButton() {
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope,
  ) {
    AppTheme {
      Surface {
        Text(
          text = "Click the FAB to expand",
          style = MaterialTheme.typography.headlineLarge,
          modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
        )

        ExpandableFloatActionButton(
          buttons = listOf(
            FloatingActionButtonItem(
              icon = IconWrapper.Vector(Icons.Filled.Brush),
              label = UIText.StringText("Brush"),
              contentDescription = UIText.StringText("Add"),
              onClick = {},
            ),
            FloatingActionButtonItem(
              icon = IconWrapper.Vector(Icons.Filled.Adb),
              label = UIText.StringText("Adb"),
              contentDescription = UIText.StringText("Add"),
              onClick = {},
            ),
            FloatingActionButtonItem(
              icon = IconWrapper.Vector(Icons.Filled.Call),
              label = UIText.StringText("Call"),
              contentDescription = UIText.StringText("Add"),
              onClick = {},
            ),
          ),
        )
      }
    }
  }
}
