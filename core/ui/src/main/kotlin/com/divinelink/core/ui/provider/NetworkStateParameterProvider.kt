package com.divinelink.core.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.model.network.NetworkState

class NetworkStateParameterProvider : PreviewParameterProvider<NetworkState> {
  override val values: Sequence<NetworkState> = sequenceOf(
    NetworkState.Offline.Initial,
    NetworkState.Offline.Persistent,
    NetworkState.Online.Initial,
    NetworkState.Online.Persistent,
  )
}
