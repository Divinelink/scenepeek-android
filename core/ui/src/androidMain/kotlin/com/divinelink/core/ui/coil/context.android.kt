package com.divinelink.core.ui.coil

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.PlatformContext

@Composable
actual fun platformContext(): PlatformContext = LocalContext.current
