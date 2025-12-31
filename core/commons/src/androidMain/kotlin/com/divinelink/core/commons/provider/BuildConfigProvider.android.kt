package com.divinelink.core.commons.provider

import com.divinelink.core.android.BuildConfig

class AndroidBuildConfigProvider : BuildConfigProvider {
  override val versionName: String get() = BuildConfig.VERSION_NAME
  override val versionCode: Int get() = BuildConfig.VERSION_CODE
  override val isDebug: Boolean get() = BuildConfig.DEBUG
  override val buildType: String get() = BuildConfig.BUILD_TYPE
  override val versionData: String
    get() = buildString {
      append("Android ")
      append(BuildConfig.VERSION_NAME)
      if (BuildConfig.DEBUG) append(" DEBUG")
      append(" (${BuildConfig.VERSION_CODE})")
    }
}

actual fun getBuildConfigProvider(): BuildConfigProvider = AndroidBuildConfigProvider()
