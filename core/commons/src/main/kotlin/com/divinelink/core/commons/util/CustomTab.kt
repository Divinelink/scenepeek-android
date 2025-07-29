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
) {
  val packageName = CustomTabsClient.getPackageName(context, null)

  val customTabsIntent = CustomTabsIntent.Builder()
    .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
    .setShowTitle(true)
    .build()

  if (packageName != null) {
    customTabsIntent.intent.setPackage(packageName)
  }

  customTabsIntent.launchUrl(context, url.toUri())
}

@ExcludeFromKoverReport
fun launchCustomTab(
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
