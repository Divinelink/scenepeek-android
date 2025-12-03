package com.divinelink.core.ui.snackbar.controller

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * https://afigaliyev.medium.com/snackbar-state-management-best-practices-for-jetpack-compose-1a5963d86d98
 */

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
  error("No SnackbarController provided.")
}
