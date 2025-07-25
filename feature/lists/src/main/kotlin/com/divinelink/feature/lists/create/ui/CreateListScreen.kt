package com.divinelink.feature.lists.create.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.feature.lists.create.CreateListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimatedVisibilityScope.CreateListScreen(
  onNavigateUp: () -> Unit,
  viewModel: CreateListViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigateUp()
    }
  }

  CreateListScaffold(
    onNavigateUp = onNavigateUp,
    uiState = uiState,
    action = viewModel::onAction,
  )
}
