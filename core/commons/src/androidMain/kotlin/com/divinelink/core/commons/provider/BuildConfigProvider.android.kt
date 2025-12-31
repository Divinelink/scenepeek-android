package com.divinelink.core.commons.provider

import com.divinelink.core.android.AndroidConstants

class AndroidBuildConfigProvider : BuildConfigProvider {
  override val versionName: String = AndroidConstants.VERSION_NAME
  override val versionCode: Int = AndroidConstants.versionCode
  override val isDebug: Boolean = AndroidConstants.isDebug
  override val buildType: String = AndroidConstants.BUILD_TYPE
  override val versionData: String = buildString {
    append("Android ")
    append(AndroidConstants.VERSION_NAME)
    if (AndroidConstants.isDebug) append(" DEBUG")
    append(" (${AndroidConstants.versionCode})")
  }
}

actual fun getBuildConfigProvider(): BuildConfigProvider = AndroidBuildConfigProvider()
