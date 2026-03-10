package com.divinelink.core.commons.provider

interface BuildConfigProvider {
  val isDebug: Boolean
  val buildType: String
  val versionCode: Int
  val versionName: String
  val versionData: String
  val versionCheckerUrl: String?
}

expect fun getBuildConfigProvider(): BuildConfigProvider
