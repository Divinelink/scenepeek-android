package com.divinelink.core.ui.manager

import androidx.compose.runtime.Composable
import com.divinelink.core.commons.provider.BuildConfigProvider

@Composable
actual fun rememberIntentManager(
  buildConfigProvider: BuildConfigProvider,
): IntentManager = IOSIntentManager(
  packageName = "",
)


class IOSIntentManager(override val packageName: String) : IntentManager {
  override fun startActivity(uri: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun shareText(text: String) {
    TODO("Not yet implemented")
  }

  override fun shareErrorReport(throwable: Throwable) {
    TODO("Not yet implemented")
  }

  override fun navigateToAppSettings() {
    TODO("Not yet implemented")
  }
}
