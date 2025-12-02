package com.divinelink.core.ui.manager

import androidx.compose.runtime.Composable
import com.divinelink.core.commons.provider.BuildConfigProvider
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController
import kotlin.experimental.ExperimentalNativeApi

@Composable
actual fun rememberIntentManager(buildConfigProvider: BuildConfigProvider): IntentManager =
  IOSIntentManager(
    packageName = "",
    buildConfigProvider = buildConfigProvider,
  )

class IOSIntentManager(
  override val packageName: String,
  private val buildConfigProvider: BuildConfigProvider,
) : IntentManager {
  override fun startActivity(uri: String): Boolean = false

  override fun shareText(text: String) {
    val currentViewController = UIApplication.sharedApplication.keyWindow?.rootViewController

    val activityViewController = UIActivityViewController(
      activityItems = listOf(text),
      applicationActivities = null,
    )

    activityViewController.popoverPresentationController?.sourceView = currentViewController?.view

    currentViewController?.presentViewController(
      viewControllerToPresent = activityViewController,
      animated = true,
      completion = null,
    )
  }

  @OptIn(ExperimentalNativeApi::class)
  override fun shareErrorReport(throwable: Throwable) = shareText(
    StringBuilder()
      .append("Stacktrace:\n")
      .append("$throwable\n")
      .apply { throwable.getStackTrace().forEach { error -> append("\t$error\n") } }
      .append("\n")
      .append("Version: ${buildConfigProvider.versionData}\n")
      .append("\n")
      .toString(),
  )

  override fun navigateToAppSettings() {
    // Do nothing
  }
}
