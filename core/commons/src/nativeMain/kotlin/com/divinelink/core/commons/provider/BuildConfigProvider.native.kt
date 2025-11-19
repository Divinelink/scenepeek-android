package com.divinelink.core.commons.provider

class NativeBuildConfigProvider(
  override val versionName: String = "1.2.3",
  override val buildType: String = "debug",
  override val isDebug: Boolean = true,
  override val versionData: String = "Test 123",
  override val versionCode: Int = 1,
) : BuildConfigProvider

actual fun getBuildConfigProvider(): BuildConfigProvider = NativeBuildConfigProvider()
