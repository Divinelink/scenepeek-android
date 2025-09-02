package com.divinelink.core.ui.composition

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.core.fixtures.core.commons.PreviewBuildConfigProvider
import com.divinelink.core.ui.manager.createIntentManager
import com.divinelink.core.ui.snackbar.controller.LocalSnackbarController
import com.divinelink.core.ui.snackbar.controller.SnackbarController
import kotlinx.coroutines.CoroutineScope

@Composable
fun LocalProvider(
  snackbarHostState: SnackbarHostState,
  coroutineScope: CoroutineScope,
  buildConfigProvider: BuildConfigProvider,
  context: Context = LocalContext.current,
  content: @Composable () -> Unit,
) {
  CompositionLocalProvider(
    LocalIntentManager provides createIntentManager(
      context = context,
      buildConfigProvider = buildConfigProvider,
    ),
    LocalSnackbarController provides SnackbarController(
      snackbarHostState = snackbarHostState,
      coroutineScope = coroutineScope,
    ),
    content = content,
  )
}

/**
 * A Local provider implementation intended to be used ONLY on preview composables and ui tests.
 */
@Composable
fun PreviewLocalProvider(content: @Composable () -> Unit) {
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  LocalProvider(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope,
    buildConfigProvider = PreviewBuildConfigProvider(),
    content = content,
  )
}
