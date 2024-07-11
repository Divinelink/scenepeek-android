package com.divinelink.core.commons.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity

object AppSettingsUtil {
  private const val PACKAGE_SCHEME = "package"

  fun openAppDetails(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
      data = Uri.fromParts(PACKAGE_SCHEME, context.packageName, null)
    }
    startActivity(context, intent, null)
  }
}
