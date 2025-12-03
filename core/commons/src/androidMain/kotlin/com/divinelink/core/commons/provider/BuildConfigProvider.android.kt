package com.divinelink.core.commons.provider

import com.divinelink.core.commons.BuildConfig

class AndroidBuildConfigProvider : BuildConfigProvider {
  override val versionName: String = BuildConfig.VERSION_NAME
  override val versionCode: Int = BuildConfig.VERSION_CODE
  override val isDebug: Boolean = BuildConfig.DEBUG
  override val buildType: String = BuildConfig.BUILD_TYPE
  override val versionData: String = buildString {
    append("Android ")
    append(BuildConfig.VERSION_NAME)
    if (BuildConfig.DEBUG) append(" DEBUG")
    append(" (${BuildConfig.VERSION_CODE})")
  }
}

actual fun getBuildConfigProvider(): BuildConfigProvider = AndroidBuildConfigProvider()
