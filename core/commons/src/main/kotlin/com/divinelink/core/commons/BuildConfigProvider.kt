package com.divinelink.core.commons

interface BuildConfigProvider {
  val isDebug: Boolean
  val buildType: String
}

object DefaultBuildConfigProvider : BuildConfigProvider {
  override val isDebug: Boolean = BuildConfig.DEBUG
  override val buildType: String = BuildConfig.BUILD_TYPE
}
