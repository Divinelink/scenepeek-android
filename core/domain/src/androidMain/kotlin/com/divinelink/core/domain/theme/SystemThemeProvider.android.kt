package com.divinelink.core.domain.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
actual fun isSystemThemeAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
