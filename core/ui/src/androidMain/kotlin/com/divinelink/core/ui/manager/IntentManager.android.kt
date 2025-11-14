package com.divinelink.core.ui.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
    context
  }
}

@Composable
actual fun getIntentManager(
  buildConfigProvider: BuildConfigProvider,
): IntentManager {
  val context = LocalContext.current

  return AndroidIntentManager(
    context = context,
    buildConfigProvider = buildConfigProvider,
  )
}
