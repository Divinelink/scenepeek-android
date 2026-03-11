package com.divinelink.core.data.app

import com.divinelink.core.model.app.InstallSource

actual class InstallSourceDetector {
  actual fun getInstallSource(): InstallSource = InstallSource.AppStore
}
