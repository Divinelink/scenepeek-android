package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.feature.add.to.account.list.AddToListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimatedVisibilityScope.AddToListScreen(
  onNavigateUp: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
  onNavigateToCreateList: () -> Unit,
  viewModel: AddToListViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.navigateToTMDBAuth.collect {
      onNavigateToTMDBAuth()
    }
  }

  AddToListScaffold(
    onNavigateUp = onNavigateUp,
    onNavigateToCreateList = onNavigateToCreateList,
    uiState = uiState,
    action = viewModel::onAction,
  )
}
