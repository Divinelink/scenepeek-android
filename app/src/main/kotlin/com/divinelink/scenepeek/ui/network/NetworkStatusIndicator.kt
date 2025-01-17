package com.divinelink.scenepeek.ui.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.scenepeek.R

@Composable
fun NetworkStatusIndicator(networkState: NetworkState) {
  val text = when (networkState) {
    is NetworkState.Offline -> stringResource(R.string.not_connected)
    else -> stringResource(R.string.connected)
  }

  AnimatedVisibility(
    visible = networkState is NetworkState.Offline || networkState is NetworkState.Online.Initial,
  ) {
    val color = animateColorAsState(
      targetValue = when (networkState) {
        is NetworkState.Offline.Initial -> MaterialTheme.colorScheme.primary
        is NetworkState.Online.Initial -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
      },
      label = "NetworkStatusIndicatorColor",
    )

    StatusMessage(
      text = text,
      containerColor = color.value,
    )
  }
}

@Composable
private fun StatusMessage(
  text: String,
  containerColor: Color,
  modifier: Modifier = Modifier,
) {
  Surface(
    color = containerColor,
    modifier = modifier.fillMaxWidth(),
  ) {
    Text(
      text = text,
      color = contentColorFor(containerColor),
      style = MaterialTheme.typography.labelSmall,
      textAlign = TextAlign.Center,
      modifier = modifier
        .padding(top = MaterialTheme.dimensions.keyline_8)
        .windowInsetsPadding(WindowInsets.navigationBars),
    )
  }
}
