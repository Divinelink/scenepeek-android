package com.divinelink.core.ui.credit

import android.os.Build

actual fun shouldHideEpisodeCount(): Boolean = Build.VERSION.SDK_INT <= Build.VERSION_CODES.S
