package com.divinelink.core.commons.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity

object AppSettingsUtil {
  private const val PACKAGE_SCHEME = "package"

  fun openAppDetails(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS)
    } else {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    }

    intent.apply {
      data = Uri.fromParts(PACKAGE_SCHEME, context.packageName, null)
    }
    startActivity(context, intent, null)
  }
}
