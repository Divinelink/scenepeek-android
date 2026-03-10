package com.divinelink.feature.updater

sealed interface UpdaterAction {
  data object Dismiss : UpdaterAction
}

