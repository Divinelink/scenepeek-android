package com.divinelink.core.fixtures.core.commons

import com.divinelink.core.commons.provider.BuildConfigProvider

class PreviewBuildConfigProvider : BuildConfigProvider {
  override val isDebug: Boolean = false
  override val buildType: String = "Release"
  override val versionCode: Int = 25
  override val versionName: String = "0.17.0"
  override val versionData: String = "0.17.0 25"
}
