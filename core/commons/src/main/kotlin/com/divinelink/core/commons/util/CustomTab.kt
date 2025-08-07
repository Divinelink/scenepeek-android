package com.divinelink.core.commons.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.divinelink.core.commons.ExcludeFromKoverReport

@ExcludeFromKoverReport
fun launchCustomTab(
  context: Context,
  url: String,
  webViewFallback: () -> Unit,
) {
  if (isCustomTabSupported(context)) {
    val packageName = CustomTabsClient.getPackageName(context, null)

    val customTabsIntent = CustomTabsIntent.Builder()
      .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
      .setShowTitle(true)
      .build()

    if (packageName != null) {
      customTabsIntent.intent.setPackage(packageName)
    }

    customTabsIntent.launchUrl(context, url.toUri())
  } else {
    val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
    if (browserIntent.resolveActivity(context.packageManager) != null) {
      context.startActivity(browserIntent)
    } else {
      webViewFallback()
    }
  }
}

@ExcludeFromKoverReport
fun launchCustomTab(
  context: Context,
  url: String,
  launcher: ActivityResultLauncher<Intent>,
  webViewFallback: () -> Unit,
) {
  if (isCustomTabSupported(context)) {
    customTab(
      context = context,
      url = url,
      launcher = launcher,
    )
  } else {
    val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
    if (browserIntent.resolveActivity(context.packageManager) != null) {
      context.startActivity(browserIntent)
    } else {
      webViewFallback()
    }
  }
}

@ExcludeFromKoverReport
private fun customTab(
  context: Context,
  url: String,
  launcher: ActivityResultLauncher<Intent>,
) {
  val packageName = CustomTabsClient.getPackageName(context, null)

  val customTabsIntent = CustomTabsIntent.Builder().build()
  val intent = customTabsIntent.intent.apply {
    data = url.toUri()
  }

  if (packageName != null) {
    customTabsIntent.intent.setPackage(packageName)
  }

  launcher.launch(intent)
}

@ExcludeFromKoverReport
private fun isCustomTabSupported(context: Context): Boolean {
  val packageName = CustomTabsClient.getPackageName(context, null)
  return packageName != null
}
