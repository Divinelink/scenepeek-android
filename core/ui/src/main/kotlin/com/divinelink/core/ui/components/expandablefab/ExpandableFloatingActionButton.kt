package com.divinelink.core.ui.components.expandablefab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.Previews

@Composable
fun ExpandableFloatActionButton(buttons: List<FloatingActionButtonItem>) {
  var expanded by remember { mutableStateOf(false) }
  val rotationState by animateFloatAsState(
    targetValue = if (expanded) 45f else 0f,
    animationSpec = tween(durationMillis = 300),
    label = "FAB Rotation Animation",
  )

  Box(modifier = Modifier.fillMaxSize()) {
    AnimatedVisibility(
      visible = expanded,
      enter = fadeIn(animationSpec = tween(durationMillis = 300)),
      exit = fadeOut(animationSpec = tween(durationMillis = 300)),
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
          .clickable { expanded = false },
      )
    }

    Column(
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.Bottom,
      modifier = Modifier
        .fillMaxSize()
        .padding(MaterialTheme.dimensions.keyline_16),
    ) {
      StaggeredFloatingActionsButtons(
        fabItems = buttons,
        expanded = expanded,
        onDismiss = { expanded = false },
      )

      Spacer(modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_8))

      FloatingActionButton(
        onClick = { expanded = !expanded },
        modifier = Modifier.rotate(rotationState),
      ) {
        Icon(Icons.Filled.Add, contentDescription = "Expandable FAB")
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
            onDismiss()
            fabItem.onClick
          },
        ),
    ) {
      Text(
        text = fabItem.label,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(start = MaterialTheme.dimensions.keyline_8),
      )
      SmallFloatingActionButton(
        onClick = {
          onDismiss()
          fabItem.onClick
        },
      ) {
        Icon(fabItem.icon, contentDescription = fabItem.contentDescription)
      }
    }
  }
}

@Previews
@Composable
fun ExpandableFloatingActionButton() {
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
            icon = Icons.Filled.Brush,
            label = "Brush",
            contentDescription = "Add",
            onClick = {},
          ),
          FloatingActionButtonItem(
            icon = Icons.Filled.Adb,
            label = "Adb",
            contentDescription = "Add",
            onClick = {},
          ),
          FloatingActionButtonItem(
            icon = Icons.Filled.Call,
            label = "Call",
            contentDescription = "Add",
            onClick = {},
          ),
        ),
      )
    }
  }
}
