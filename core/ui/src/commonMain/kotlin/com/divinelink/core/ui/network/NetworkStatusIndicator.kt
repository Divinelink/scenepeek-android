package com.divinelink.core.ui.network

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.network.NetworkState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_connected
import com.divinelink.core.ui.core_ui_not_connected
import com.divinelink.core.ui.provider.NetworkStateParameterProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun NetworkStatusIndicator(networkState: NetworkState) {
  val text = when (networkState) {
    is NetworkState.Offline -> stringResource(UiString.core_ui_not_connected)
    else -> stringResource(UiString.core_ui_connected)
  }

  AnimatedVisibility(
    visible = networkState is NetworkState.Offline || networkState is NetworkState.Online.Initial,
  ) {
    val color by animateColorAsState(
      targetValue = when (networkState) {
        is NetworkState.Offline.Initial -> MaterialTheme.colorScheme.primary
        is NetworkState.Online.Initial -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
      },
      label = "NetworkStatusIndicatorColor",
    )

    StatusMessage(
      text = text,
      containerColor = color,
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
      style = MaterialTheme.typography.labelMedium,
      textAlign = TextAlign.Center,
      modifier = modifier
        .padding(
          top = MaterialTheme.dimensions.keyline_16,
          bottom = MaterialTheme.dimensions.keyline_4,
        )
        .windowInsetsPadding(WindowInsets.navigationBars),
    )
  }
}

@Previews
@Composable
fun NetworkStatusIndicatorPreview(
  @PreviewParameter(NetworkStateParameterProvider::class) state: NetworkState,
) {
  AppTheme {
    NetworkStatusIndicator(state)
  }
}
