package com.divinelink.feature.season.ui

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
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.feature.season.SeasonViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.SeasonScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: SeasonViewModel = koinViewModel(),
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

  val surfaceColor = MaterialTheme.colorScheme.surface
  DisposableEffect(textColor) {
    val isLight = textColor == surfaceColor
    systemUiController.setStatusBarColor(isLight = !isLight && !isDarkTheme)
    onDispose {
      systemUiController.setStatusBarColor(isLight = !isDarkTheme)
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
        text = UIText.StringText(uiState.season?.name ?: ""),
        subtitle = UIText.StringText(uiState.title),
        progress = toolbarProgress,
        onNavigateUp = { onNavigate(Navigation.Back) },
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent,
          scrolledContainerColor = Color.Transparent,
        ),
      )
    },
    floatingActionButton = {
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        SeasonContent(
          visibilityScope = this@SeasonScreen,
          uiState = uiState,
          onBackdropLoaded = { onBackdropLoaded = true },
          toolbarProgress = { progress -> toolbarProgress = progress },
          action = { action ->
          },
          onNavigate = onNavigate,
        )
      }
    },
  )
}
