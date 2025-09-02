package com.divinelink.core.ui.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.core.commons.ExcludeFromKoverReport

@ExcludeFromKoverReport
internal class AndroidIntentManager(
  private val context: Context,
  private val buildConfigProvider: BuildConfigProvider,
) : IntentManager {

  override val packageName: String
    get() = context.packageName

  override fun startActivity(intent: Intent): Boolean = try {
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
    startActivity(Intent.createChooser(sendIntent, null))
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
}
