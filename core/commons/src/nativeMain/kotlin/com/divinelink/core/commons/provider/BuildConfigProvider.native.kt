package com.divinelink.core.commons.provider

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

private val mainBundle = NSBundle.mainBundle
private val CFBundleShortVersionString = mainBundle
  .objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
private val CFBundleVersion = mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String

@OptIn(ExperimentalNativeApi::class)
class NativeBuildConfigProvider(
  override val versionName: String = CFBundleShortVersionString ?: "Unknown",
  override val isDebug: Boolean = Platform.isDebugBinary,
  override val buildType: String = if (isDebug) "debug" else "release",
  override val versionCode: Int = CFBundleVersion?.toIntOrNull() ?: 0,
  override val versionData: String = buildString {
    append("iOS ")
    append(versionName)
    if (isDebug) append(" DEBUG")
    append(" ($versionCode)")
  },
) : BuildConfigProvider

actual fun getBuildConfigProvider(): BuildConfigProvider = NativeBuildConfigProvider()
