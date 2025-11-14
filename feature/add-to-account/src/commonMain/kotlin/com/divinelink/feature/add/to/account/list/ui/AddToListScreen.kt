package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.add.to.account.list.AddToListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimatedVisibilityScope.AddToListScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: AddToListViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.navigateToTMDBAuth.collect {
      onNavigate(Navigation.TMDBAuthRoute)
    }
  }

  AddToListScaffold(
    onNavigateUp = { onNavigate(Navigation.Back) },
    onNavigateToCreateList = { onNavigate(Navigation.CreateListRoute) },
    uiState = uiState,
    action = viewModel::onAction,
  )
}
