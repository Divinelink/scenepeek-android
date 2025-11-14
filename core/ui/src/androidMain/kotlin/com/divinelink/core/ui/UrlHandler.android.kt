package com.divinelink.core.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.divinelink.core.commons.ExcludeFromKoverReport


@Composable
actual fun rememberUrlHandler(): UrlHandler {
  val context = LocalContext.current
  return remember(context) { AndroidUrlHandler(context) }
}

@Composable
actual fun rememberUrlHandlerWithResult(onResult: () -> Unit): UrlHandler {
  val context = LocalContext.current

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
  ) {
    onResult()
  }

  return remember(context) {
    AndroidUrlHandlerWithResult(
      context = context,
      launcher = launcher,
    )
  }
}

class AndroidUrlHandler(private val context: Context) : UrlHandler {
  override fun openUrl(url: String, onError: () -> Unit) {
    launchCustomTab(
      context = context,
      url = url,
      webViewFallback = onError,
    )
  }
}

class AndroidUrlHandlerWithResult(
  private val context: Context,
  private val launcher: ActivityResultLauncher<Intent>,
) : UrlHandler {
  override fun openUrl(url: String, onError: () -> Unit) {
    launchCustomTab(
      context = context,
      launcher = launcher,
      url = url,
      webViewFallback = onError,
    )
  }
}

@ExcludeFromKoverReport
private fun launchCustomTab(
  context: Context,
  launcher: ActivityResultLauncher<Intent>,
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

    launcher.launch(
      customTabsIntent.intent.apply { data = url.toUri() },
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
private fun launchCustomTab(
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
private fun isCustomTabSupported(context: Context): Boolean {
  val packageName = CustomTabsClient.getPackageName(context, null)
  return packageName != null
}
