package com.divinelink.feature.tmdb.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.divinelink.core.commons.util.launchCustomTab
import com.divinelink.core.ui.components.LoadingContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun TMDBAuthScreen(
  onNavigateUp: () -> Unit,
  viewModel: TMDBAuthViewModel = koinViewModel(),
) {
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigateUp()
    }
  }

  val customTabLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
  ) {
    viewModel.handleCloseWeb()
  }

  LaunchedEffect(Unit) {
    viewModel.openUrlTab.collect { url ->
      launchCustomTab(context = context, url = url, launcher = customTabLauncher)
    }
  }

  LoadingContent()
}
