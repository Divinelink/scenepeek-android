package com.divinelink.core.domain.theme

import platform.UIKit.UIDevice

actual fun isSystemThemeAvailable(): Boolean {
  val systemVersion = UIDevice.currentDevice.systemVersion
  val majorVersion = systemVersion.split(".").firstOrNull()?.toIntOrNull() ?: 0
  return majorVersion >= 13
}
