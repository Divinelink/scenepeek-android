package com.divinelink.scenepeek.ui.network

sealed interface NetworkState {
  sealed interface Online : NetworkState {
    data object Initial : Online
    data object Persistent : Online
  }

  sealed interface Offline : NetworkState {
    data object Initial : Offline
    data object Persistent : Offline
  }
}
