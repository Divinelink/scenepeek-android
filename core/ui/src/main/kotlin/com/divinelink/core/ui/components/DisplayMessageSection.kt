package com.divinelink.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.ui.getString
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun DisplayMessageSection(
  message: DisplayMessage?,
  onTimeout: () -> Unit,
) {
  var progress by remember { mutableFloatStateOf(1f) }
  val currentJob = remember { mutableStateOf<Job?>(null) }
  val scope = rememberCoroutineScope()

  val containerColor by animateColorAsState(
    targetValue = when (message) {
      is DisplayMessage.Success -> MaterialTheme.colorScheme.primaryContainer
      is DisplayMessage.Error -> MaterialTheme.colorScheme.errorContainer
      else -> MaterialTheme.colorScheme.surfaceContainerLow
    },
    label = "Container color animation",
  )

  val primary by animateColorAsState(
    targetValue = when (message) {
      is DisplayMessage.Success -> MaterialTheme.colorScheme.primary
      is DisplayMessage.Error -> MaterialTheme.colorScheme.error
      else -> MaterialTheme.colorScheme.surface
    },
    label = "Primary color animation",
  )

  val color by animateColorAsState(
    targetValue = when (message) {
      is DisplayMessage.Success -> MaterialTheme.colorScheme.onPrimaryContainer
      is DisplayMessage.Error -> MaterialTheme.colorScheme.onErrorContainer
      else -> MaterialTheme.colorScheme.onSurfaceVariant
    },
    label = "Color animation",
  )

  LaunchedEffect(message) {
    progress = 1f
    currentJob.value?.cancel()
    currentJob.value = scope.launch {
      animate(1f, 0f, animationSpec = tween(5000)) { value, _ ->
        progress = value
      }
      onTimeout()
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      currentJob.value?.cancel()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(MaterialTheme.dimensions.keyline_8)
      .clip(MaterialTheme.shapes.medium)
      .clickable { onTimeout() }
      .background(containerColor),
  ) {
    Text(
      text = AnnotatedString.fromHtml(message?.message?.getString() ?: ""),
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_16),
      color = color,
      style = MaterialTheme.typography.bodySmall,
    )
    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier.fillMaxWidth(),
      color = primary,
      trackColor = containerColor,
    )
  }
}
