package com.divinelink.feature.lists.create.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.feature.lists.create.CreateListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimatedVisibilityScope.CreateListScreen(
  onNavigateUp: () -> Unit,
  onNavigateBackToLists: () -> Unit,
  viewModel: CreateListViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigateUp()
    }
  }

  LaunchedEffect(Unit) {
    viewModel.onNavigateBackToLists.collect {
      onNavigateBackToLists()
    }
  }

  CreateListScaffold(
    onNavigateUp = onNavigateUp,
    uiState = uiState,
    action = viewModel::onAction,
  )
}
