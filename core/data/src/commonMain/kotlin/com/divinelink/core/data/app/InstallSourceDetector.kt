package com.divinelink.core.data.app

import com.divinelink.core.model.app.InstallSource

expect class InstallSourceDetector {
  fun getInstallSource(): InstallSource
}
