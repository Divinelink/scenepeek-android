package com.divinelink.core.data.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.divinelink.core.model.app.InstallSource

actual class InstallSourceDetector(val context: Context) {

  actual fun getInstallSource(): InstallSource {
    try {
      val packageName = context.packageName
      val packageManager = context.packageManager
      val installedPackageName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName
      } else {
        @Suppress("DEPRECATION")
        packageManager.getInstallerPackageName(packageName)
      }

      return InstallSource.from(installedPackageName)
    } catch (_: PackageManager.NameNotFoundException) {
      return InstallSource.Github
    }
  }
}
