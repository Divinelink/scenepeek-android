package com.divinelink.core.commons

interface BuildConfigProvider {
  val isDebug: Boolean
  val buildType: String
  val versionName: String
  val versionData: String
}
