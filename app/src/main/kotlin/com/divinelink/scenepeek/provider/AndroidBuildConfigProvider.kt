package com.divinelink.scenepeek.provider

import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.scenepeek.BuildConfig

class AndroidBuildConfigProvider : BuildConfigProvider {
  override val buildType: String
    get() = BuildConfig.BUILD_TYPE
  override val isDebug: Boolean
    get() = BuildConfig.DEBUG
  override val versionName: String
    get() = BuildConfig.VERSION_NAME
  override val versionData: String
    get() = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
}
