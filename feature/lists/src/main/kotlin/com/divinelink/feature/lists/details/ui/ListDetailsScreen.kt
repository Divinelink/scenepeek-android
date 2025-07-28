package com.divinelink.feature.lists.details.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.updateStatusBarColor
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.EditListRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.feature.lists.details.ListDetailsAction
import com.divinelink.feature.lists.details.ListDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.ListDetailsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToMediaDetails: (DetailsRoute) -> Unit,
  onNavigateToEdit: (EditListRoute) -> Unit,
  viewModel: ListDetailsViewModel = koinViewModel(),
) {
  val view = LocalView.current
  val isDarkTheme = LocalDarkThemeProvider.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  var isAppBarVisible by remember { mutableStateOf(false) }
  var onBackdropLoaded by remember { mutableStateOf(false) }

  val containerColor by animateColorAsState(
    targetValue = when (isAppBarVisible) {
      true -> MaterialTheme.colorScheme.surface
      false -> Color.Transparent
    },
    animationSpec = tween(durationMillis = 0),
    label = "TopAppBar Container Color",
  )

  val textColor = when {
    // When app bar is visible, we want to contrast against the app bar background
    isAppBarVisible -> MaterialTheme.colorScheme.onSurface

    // When backdrop has loaded, determine color based on theme
    onBackdropLoaded -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.surface
    }

    // When backdrop hasn't loaded yet, use default text colors
    else -> if (LocalDarkThemeProvider.current) {
      MaterialTheme.colorScheme.onSurface
    } else {
      MaterialTheme.colorScheme.onSurface // Changed this to onSurface to ensure contrast
    }
  }

  val surfaceColor = MaterialTheme.colorScheme.surface
  DisposableEffect(textColor) {
    val isLight = textColor == surfaceColor
    updateStatusBarColor(view = view, setLight = !isLight && !isDarkTheme)

    onDispose {
      // Reset the status bar color when the composable is disposed
      updateStatusBarColor(view = view, setLight = !isDarkTheme)
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.testTag(TestTags.Lists.DETAILS_SCREEN),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        modifier = Modifier.background(containerColor),
        scrollBehavior = scrollBehavior,
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = Color.Transparent,
          containerColor = Color.Transparent,
        ),
        contentColor = textColor,
        text = UIText.StringText(uiState.details.name),
        isVisible = isAppBarVisible,
        onNavigateUp = onNavigateUp,
      )
    },
    floatingActionButton = {
      AnimatedVisibility(
        visible = !uiState.multipleSelectMode,
        enter = fadeIn(tween(easing = EaseIn)),
        exit = fadeOut(tween(easing = EaseOut)),
      ) {
        ScaffoldFab(
          modifier = Modifier.testTag(TestTags.Lists.CREATE_LIST_FAB),
          icon = Icons.Default.Edit,
          text = null,
          expanded = false,
          onClick = {
            onNavigateToEdit(
              EditListRoute(
                id = uiState.id,
                name = uiState.details.name,
                backdropPath = uiState.details.backdropPath,
                description = uiState.details.description,
                public = uiState.details.public,
              ),
            )
          },
        )
      }
    },
    content = { paddingValues ->
      Column {
        AnimatedVisibility(!onBackdropLoaded) {
          Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
        }

        ListDetailsContent(
          state = uiState,
          action = { action ->
            when (action) {
              ListDetailsAction.LoadMore,
              ListDetailsAction.Refresh,
              ListDetailsAction.OnDeselectAll,
              ListDetailsAction.OnSelectAll,
              ListDetailsAction.OnDismissMultipleSelect,
              is ListDetailsAction.SelectMedia,
              -> viewModel.onAction(action)
              is ListDetailsAction.OnItemClick -> onNavigateToMediaDetails(
                DetailsRoute(
                  id = action.mediaId,
                  mediaType = action.mediaType,
                  isFavorite = null,
                ),
              )
            }
          },
          onShowTitle = { show ->
            isAppBarVisible = show
          },
          onBackdropLoaded = {
            onBackdropLoaded = true
          },
        )
      }
    },
  )
}
