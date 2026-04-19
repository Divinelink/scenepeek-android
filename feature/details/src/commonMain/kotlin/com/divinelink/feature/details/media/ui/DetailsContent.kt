package com.divinelink.feature.details.media.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.rememberSystemUiController
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.app.TestAppInfoRepository
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.ScreenType
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.permission.canManageRequests
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.navigation.Navigator
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.core.ui.components.extensions.collapsingScrollConnection
import com.divinelink.core.ui.components.modal.jellyseerr.manage.ManageJellyseerrMediaModal
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.list.LazyColumnWithOffset
import com.divinelink.core.ui.menu.DropdownMenuButton
import com.divinelink.core.ui.resources.core_ui_okay
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.details.media.ui.components.DetailActions
import com.divinelink.feature.details.media.ui.components.HeaderDetails
import com.divinelink.feature.details.media.ui.components.MediaDetailsPager
import com.divinelink.feature.details.media.ui.fab.DetailsExpandableFloatingActionButton
import com.divinelink.feature.details.media.ui.provider.DetailsViewStateProvider
import com.divinelink.feature.request.media.RequestMediaModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
  viewState: DetailsViewState,
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  onMarkAsFavoriteClicked: () -> Unit,
  onMediaItemClick: (MediaItem) -> Unit,
  onPersonClick: (Person) -> Unit,
  onConsumeSnackbar: () -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  onViewAllCreditsClick: () -> Unit,
  onShowAllRatingsClick: () -> Unit,
  onTabSelected: (Int) -> Unit,
  onPlayTrailerClick: (String) -> Unit,
  onDeleteRequest: (Int) -> Unit,
  onDeleteMedia: (Boolean) -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
) {
  val systemUiController = rememberSystemUiController()
  val isDarkTheme = LocalDarkThemeProvider.current
  val scope = rememberCoroutineScope()

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  var toolbarProgress by remember { mutableFloatStateOf(0F) }
  var onBackdropLoaded by remember { mutableStateOf(false) }
  var showRequestModal by remember { mutableStateOf(false) }
  var showManageMediaModal by rememberSaveable { mutableStateOf(false) }

  SnackbarMessageHandler(
    snackbarMessage = viewState.snackbarMessage,
    onDismissSnackbar = onConsumeSnackbar,
  )

  if (showRequestModal) {
    RequestMediaModal(
      request = null,
      mediaType = viewState.mediaType,
      media = viewState.mediaDetails?.toMediaItem(),
      onDismissRequest = { showRequestModal = false },
      onUpdateMediaInfo = onUpdateMediaInfo,
      onNavigate = onNavigate,
    )
  }

  LaunchedEffect(viewState.jellyseerrMediaInfo) {
    if (viewState.jellyseerrMediaInfo?.status == JellyseerrStatus.Media.UNKNOWN ||
      viewState.jellyseerrMediaInfo == null
    ) {
      showManageMediaModal = false
    }
  }

  if (showManageMediaModal) {
    ManageJellyseerrMediaModal(
      requests = viewState.jellyseerrMediaInfo?.requests,
      onDismissRequest = { showManageMediaModal = false },
      onDeleteRequest = onDeleteRequest,
      isLoading = viewState.isLoading,
      mediaType = viewState.mediaType,
      onDeleteMedia = onDeleteMedia,
      showAdvancedOptions = viewState.permissions.canManageRequests,
    )
  }

  val textColor = when {
    // When app bar is visible, we want to contrast against the app bar background
    toolbarProgress > 0.5 -> MaterialTheme.colorScheme.onSurface

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
    systemUiController.setStatusBarColor(isLight = !isLight && !isDarkTheme)

    onDispose {
      // Reset the status bar color when the composable is disposed
      systemUiController.setStatusBarColor(isLight = !isDarkTheme)
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = animatedVisibilityScope,
  ).PersistentScaffold(
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        topAppBarColors = TopAppBarDefaults.topAppBarColors(
          scrolledContainerColor = Color.Transparent,
          containerColor = Color.Transparent,
        ),
        contentColor = textColor,
        text = UIText.StringText(viewState.mediaDetails?.title ?: ""),
        progress = toolbarProgress,
        actions = {
          FavoriteButton(
            modifier = Modifier.clip(MaterialTheme.shape.rounded),
            isFavorite = viewState.mediaDetails?.isFavorite ?: false,
            onClick = onMarkAsFavoriteClicked,
            inactiveColor = textColor,
          )

          DropdownMenuButton(
            color = textColor,
            screenType = when (viewState.mediaDetails) {
              is Movie -> ScreenType.Movie(
                id = viewState.mediaDetails.id,
                name = viewState.mediaDetails.title,
              )
              is TV -> ScreenType.Show(
                id = viewState.mediaDetails.id,
                name = viewState.mediaDetails.title,
                spoilersObfuscated = viewState.spoilersObfuscated,
              )
              null -> ScreenType.Unknown
            },
          )
        },
        onNavigateUp = {
          onNavigate(Navigation.Back)
          onBackdropLoaded = false
        },
      )
    },
    floatingActionButton = {
      DetailsExpandableFloatingActionButton(
        actionButtons = viewState.actionButtons,
        onAddRateClicked = onAddRateClick,
        onAddToWatchlistClicked = onAddToWatchlistClick,
        onAddToListClicked = {
          viewState.mediaItem?.let {
            onNavigate(
              Navigation.AddToListRoute(
                id = it.id,
                mediaType = it.mediaType.value,
              ),
            )
          }
        },
        onRequestClicked = { showRequestModal = true },
        onManageMovie = { showManageMediaModal = true },
        onManageTv = { showManageMediaModal = true },
      )
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        when (viewState.mediaDetails) {
          is Movie, is TV -> MediaDetailsContent(
            topPadding = paddingValues.calculateTopPadding(),
            uiState = viewState,
            onNavigate = onNavigate,
            visibilityScope = animatedVisibilityScope,
            trailer = viewState.trailer,
            onMediaItemClick = onMediaItemClick,
            onAddRateClick = onAddRateClick,
            onAddToWatchlistClick = onAddToWatchlistClick,
            viewAllCreditsClick = onViewAllCreditsClick,
            onPersonClick = onPersonClick,
            obfuscateEpisodes = viewState.spoilersObfuscated,
            viewAllRatingsClick = onShowAllRatingsClick,
            onTabSelected = onTabSelected,
            onWatchTrailer = onPlayTrailerClick,
            onShowTitle = { showTitle ->
              toolbarProgress = showTitle
            },
            onBackdropLoaded = { onBackdropLoaded = true },
            onOpenManageModal = { showManageMediaModal = true },
            onSwitchPreferences = onSwitchPreferences,
            scope = scope,
          )
          null -> {
            // Do nothing
          }
        }
        if (viewState.error != null) {
          SimpleAlertDialog(
            confirmClick = { onNavigate(Navigation.Back) },
            confirmText = UIText.ResourceText(UiString.core_ui_okay),
            uiState = AlertDialogUiState(text = viewState.error),
          )
        }
      }
      if (viewState.isLoading) {
        LoadingContent()
      }
    },
  )
}

@Composable
private fun SharedTransitionScope.MediaDetailsContent(
  topPadding: Dp,
  uiState: DetailsViewState,
  visibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
  trailer: Video?,
  obfuscateEpisodes: Boolean,
  onPersonClick: (Person) -> Unit,
  onMediaItemClick: (MediaItem) -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  viewAllCreditsClick: () -> Unit,
  viewAllRatingsClick: () -> Unit,
  onWatchTrailer: (String) -> Unit,
  onTabSelected: (Int) -> Unit,
  onShowTitle: (Float) -> Unit,
  onBackdropLoaded: () -> Unit,
  onOpenManageModal: () -> Unit,
  onSwitchPreferences: (SwitchPreferencesAction) -> Unit,
  scope: CoroutineScope,
) {
  if (uiState.mediaDetails == null) return

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTabIndex) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )
  val state = rememberLazyListState()
  val headerScrollConnection = state.collapsingScrollConnection()

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        onTabSelected(page)
      }
  }

  LazyColumnWithOffset(
    backdropPath = uiState.mediaDetails.backdropPath,
    onBackdropLoaded = onBackdropLoaded,
    posterPath = uiState.mediaDetails.posterPath,
    visibilityScope = visibilityScope,
    onNavigateToPoster = { onNavigate(Navigation.MediaPosterRoute(it)) },
    headerContent = {
      HeaderDetails(
        mediaDetails = uiState.mediaDetails,
        uiState = uiState,
        onOpenManageModal = onOpenManageModal,
        trailer = trailer,
        onWatchTrailer = onWatchTrailer,
        viewAllRatingsClick = viewAllRatingsClick,
      )
    },
    paddingOffset = topPadding,
    stickyIndex = 2,
    onScrollUpdate = onShowTitle,
    state = state,
  ) {
    item {
      DetailActions(
        onAddRateClick = onAddRateClick,
        uiState = uiState,
        onAddToWatchlistClick = onAddToWatchlistClick,
        onNavigate = onNavigate,
      )
    }

    stickyHeader {
      ScenePeekTabs(
        tabs = uiState.tabs,
        selectedIndex = selectedPage,
        onClick = { scope.launch { pagerState.animateScrollToPage(it) } },
      )
    }

    item {
      MediaDetailsPager(
        modifier = Modifier
          .fillParentMaxSize(),
        pagerState = pagerState,
        scroll = headerScrollConnection,
        uiState = uiState,
        onNavigate = onNavigate,
        mediaDetails = uiState.mediaDetails,
        onPersonClick = onPersonClick,
        obfuscateEpisodes = obfuscateEpisodes,
        viewAllCreditsClick = viewAllCreditsClick,
        onSwitchPreferences = onSwitchPreferences,
        onMediaItemClick = onMediaItemClick,
      )
    }
  }
}

@Previews
@Composable
fun DetailsContentPreview(
  @PreviewParameter(DetailsViewStateProvider::class) viewState: DetailsViewState,
) {
  SharedTransitionScopeProvider {
    val state = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = TestPreferencesRepository(),
      appInfoRepository = TestAppInfoRepository(),
      navigator = Navigator(),
    )

    ProvideScenePeekAppState(
      appState = state,
    ) {
      state.sharedTransitionScope = it
      PreviewLocalProvider {
        AppTheme {
          Surface {
            DetailsContent(
              viewState = viewState,
              animatedVisibilityScope = this,
              onNavigate = {},
              onMarkAsFavoriteClicked = {},
              onMediaItemClick = {},
              onConsumeSnackbar = {},
              onAddRateClick = {},
              onAddToWatchlistClick = {},
              onPersonClick = {},
              onViewAllCreditsClick = {},
              onShowAllRatingsClick = {},
              onTabSelected = {},
              onPlayTrailerClick = {},
              onDeleteRequest = {},
              onDeleteMedia = {},
              onSwitchPreferences = {},
              onUpdateMediaInfo = {},
            )
          }
        }
      }
    }
  }
}
