package com.divinelink.feature.episode.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.rememberSystemUiController
import com.divinelink.core.model.ScreenType
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.menu.DropdownMenuButton
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.episode.EpisodeAction
import com.divinelink.feature.episode.EpisodeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.EpisodeScreen(
  route: Navigation.EpisodeRoute,
  onNavigate: (Navigation) -> Unit,
  viewModel: EpisodeViewModel = koinViewModel { parametersOf(route) },
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  val systemUiController = rememberSystemUiController()
  var onBackdropLoaded by remember { mutableStateOf(false) }
  val isDarkTheme = LocalDarkThemeProvider.current
  var toolbarProgress by remember { mutableFloatStateOf(0F) }
  val textColor = when {
    toolbarProgress > 0.5 -> MaterialTheme.colorScheme.onSurface

    onBackdropLoaded -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.surface
    }

    else -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.onSurface
    }
  }

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = { viewModel.onAction(EpisodeAction.DismissSnackbar) },
  )

  val surfaceColor = MaterialTheme.colorScheme.surface
  DisposableEffect(textColor) {
    val isLight = textColor == surfaceColor
    systemUiController.setStatusBarColor(isLight = !isLight && !isDarkTheme)
    onDispose {
      systemUiController.setStatusBarColor(isLight = !isDarkTheme)
    }
  }

  LaunchedEffect(Unit) {
    viewModel.navigateToLogin.collect {
      onNavigate(Navigation.TMDBAuthRoute)
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        text = UIText.StringText(uiState.episode?.name ?: ""),
        subtitle = UIText.StringText(uiState.showTitle),
        contentColor = textColor,
        progress = toolbarProgress,
        onNavigateUp = { onNavigate(Navigation.Back) },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
        actions = {
          DropdownMenuButton(
            screenType = ScreenType.Episode(
              id = uiState.showId,
              name = uiState.showTitle,
              seasonNumber = uiState.seasonNumber,
              episodeNumber = uiState.episode?.number ?: -1,
            ),
          )
        },
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        EpisodeContent(
          visibilityScope = this@EpisodeScreen,
          uiState = uiState,
          topPadding = it.calculateTopPadding(),
          onBackdropLoaded = { onBackdropLoaded = true },
          toolbarProgress = { progress -> toolbarProgress = progress },
          action = viewModel::onAction,
          onNavigate = onNavigate,
        )
      }
    },
  )
}
