package com.divinelink.core.ui.coil

import androidx.compose.runtime.Composable
import coil3.PlatformContext

@Composable
actual fun platformContext(): PlatformContext = PlatformContext.INSTANCE
