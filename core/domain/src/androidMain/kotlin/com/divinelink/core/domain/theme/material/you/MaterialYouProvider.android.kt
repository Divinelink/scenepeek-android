package com.divinelink.core.domain.theme.material.you

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun isMaterialYouAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
