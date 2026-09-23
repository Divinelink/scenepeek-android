package com.divinelink.core.ui.manager

import androidx.compose.runtime.Composable
import com.divinelink.core.commons.provider.BuildConfigProvider
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSCharacterSet
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.URLQueryAllowedCharacterSet
import platform.Foundation.create
import platform.Foundation.stringByAddingPercentEncodingWithAllowedCharacters
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

  override fun launchEmail(
    email: String,
    subject: String?,
    body: String?,
  ) {
    val urlString = buildString {
      append("mailto:$email")

      val params = mutableListOf<String>()
      subject?.let { params.add("subject=${it.encodeURLComponent()}") }
      body?.let { params.add("body=${it.encodeURLComponent()}") }

      if (params.isNotEmpty()) {
        append("?")
        append(params.joinToString("&"))
      }
    }

    val url = NSURL.URLWithString(urlString) ?: return

    UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any?>()) { success ->
      if (!success) {
        println("Failed to open email app")
      }
    }
  }

  @OptIn(BetaInteropApi::class)
  private fun String.encodeURLComponent(): String =
    NSString.create(string = this).stringByAddingPercentEncodingWithAllowedCharacters(
      NSCharacterSet.URLQueryAllowedCharacterSet,
    ) ?: this

  override fun navigateToAppSettings() {
    // Do nothing
  }
}
