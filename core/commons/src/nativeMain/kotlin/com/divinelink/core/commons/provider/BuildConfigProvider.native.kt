package com.divinelink.core.commons.provider

class NativeBuildConfigProvider(
  override val versionName: String = TODO(),
  override val buildType: String = TODO(),
  override val isDebug: Boolean = TODO(),
  override val versionData: String = TODO(),
  override val versionCode: Int = TODO(),
) : BuildConfigProvider

actual fun getBuildConfigProvider(): BuildConfigProvider = NativeBuildConfigProvider()
