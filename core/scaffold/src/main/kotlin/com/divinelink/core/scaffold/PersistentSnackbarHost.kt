package com.divinelink.core.scaffold

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScaffoldState.PersistentSnackbarHost(modifier: Modifier = Modifier) {
  SnackbarHost(
    modifier = modifier
      .fillMaxWidth()
      .sharedElement(
        sharedContentState = rememberSharedContentState(
          SnackbarHostElementKey,
        ),
        animatedVisibilityScope = this,
      ),
    hostState = state.snackbarHostState,
  )
}

private data object SnackbarHostElementKey
