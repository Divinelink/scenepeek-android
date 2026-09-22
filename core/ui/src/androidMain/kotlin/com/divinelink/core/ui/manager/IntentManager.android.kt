package com.divinelink.core.ui.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.core.commons.provider.BuildConfigProvider

@ExcludeFromKoverReport
internal class AndroidIntentManager(
  private val context: Context,
  private val buildConfigProvider: BuildConfigProvider,
) : IntentManager {

  override val packageName: String
    get() = context.packageName

  override fun startActivity(uri: String): Boolean = try {
    val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
    context.startActivity(intent)
    true
  } catch (_: ActivityNotFoundException) {
    false
  }

  override fun shareText(text: String) {
    val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_TEXT, text)
      type = "text/plain"
    }

    context.startActivity(Intent.createChooser(sendIntent, null))
  }

  override fun shareErrorReport(throwable: Throwable) = shareText(
    StringBuilder()
      .append("Stacktrace:\n")
      .append("$throwable\n")
      .apply { throwable.stackTrace.forEach { append("\t$it\n") } }
      .append("\n")
      .append("Version: ${buildConfigProvider.versionData}\n")
      .append("\n")
      .toString(),
  )

  override fun navigateToAppSettings() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS)
    } else {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    }

    intent.apply {
      data = Uri.fromParts(PACKAGE_SCHEME, context.packageName, null)
    }

    try {
      context.startActivity(intent, null)
    } catch (_: Exception) {
      Toast.makeText(context, "Couldn't not open app settings", Toast.LENGTH_SHORT).show()
    }
  }

  override fun launchEmail(
    email: String,
    subject: String?,
    body: String?,
  ) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
      val uriString = buildString {
        append("mailto:$email")

        val params = mutableListOf<String>()
        subject?.let { params.add("subject=${Uri.encode(it)}") }
        body?.let { params.add("body=${Uri.encode(it)}") }

        if (params.isNotEmpty()) {
          append("?")
          append(params.joinToString("&"))
        }
      }

      data = uriString.toUri()
    }

    try {
      context.startActivity(emailIntent)
    } catch (_: Exception) {
      Toast.makeText(context, "No email app installed", Toast.LENGTH_SHORT).show()
    }
  }

  companion object {
    private const val PACKAGE_SCHEME = "package"
  }
}

@Composable
actual fun rememberIntentManager(buildConfigProvider: BuildConfigProvider): IntentManager {
  val context = LocalContext.current

  return AndroidIntentManager(
    context = context,
    buildConfigProvider = buildConfigProvider,
  )
}
