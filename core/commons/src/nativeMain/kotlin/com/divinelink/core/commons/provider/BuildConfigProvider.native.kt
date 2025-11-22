package com.divinelink.core.commons.provider

class NativeBuildConfigProvider(
  override val versionName: String = "1.2.3",
  override val buildType: String = "debug",
  override val isDebug: Boolean = true,
  override val versionCode: Int = 1,
  override val versionData: String = buildString {
    append("iOS ")
    append(versionName)
    if (isDebug) append(" DEBUG")
    append(" (${versionCode})")
  },
) : BuildConfigProvider

actual fun getBuildConfigProvider(): BuildConfigProvider = NativeBuildConfigProvider()
