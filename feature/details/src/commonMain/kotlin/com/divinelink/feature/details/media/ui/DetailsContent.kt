package com.divinelink.feature.details.media.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalDarkThemeProvider
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.rememberSystemUiController
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.DetailsForm
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.permission.canManageRequests
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.DetailsDropdownMenu
import com.divinelink.core.ui.FavoriteButton
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.core.ui.components.modal.jellyseerr.manage.ManageJellyseerrMediaModal
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.resources.core_ui_okay
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.core.ui.tab.ScenePeekTabs
import com.divinelink.feature.details.media.ui.collapsing.DynamicDetailsCollapsingToolbar
import com.divinelink.feature.details.media.ui.fab.DetailsExpandableFloatingActionButton
import com.divinelink.feature.details.media.ui.forms.about.AboutFormContent
import com.divinelink.feature.details.media.ui.forms.cast.CastFormContent
import com.divinelink.feature.details.media.ui.forms.recommendation.RecommendationsFormContent
import com.divinelink.feature.details.media.ui.forms.reviews.ReviewsFormContent
import com.divinelink.feature.details.media.ui.forms.seasons.SeasonsFormContent
import com.divinelink.feature.details.media.ui.provider.DetailsViewStateProvider
import com.divinelink.feature.request.media.RequestMediaModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
  viewState: DetailsViewState,
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  onMarkAsFavoriteClicked: () -> Unit,
  onSimilarMovieClicked: (MediaItem.Media) -> Unit,
  onPersonClick: (Person) -> Unit,
  onConsumeSnackbar: () -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  onViewAllCreditsClick: () -> Unit,
  onObfuscateSpoilers: () -> Unit,
  onShowAllRatingsClick: () -> Unit,
  onTabSelected: (Int) -> Unit,
  onPlayTrailerClick: (String) -> Unit,
  onDeleteRequest: (Int) -> Unit,
  onDeleteMedia: (Boolean) -> Unit,
  onUpdateMediaInfo: (JellyseerrMediaInfo) -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
) {
  val systemUiController = rememberSystemUiController()
  val isDarkTheme = LocalDarkThemeProvider.current
  val scope = rememberCoroutineScope()

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  var showDropdownMenu by remember { mutableStateOf(false) }
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

          IconButton(
            modifier = Modifier.testTag(TestTags.Menu.MENU_BUTTON_VERTICAL),
            onClick = { showDropdownMenu = !showDropdownMenu },
          ) {
            Icon(
              imageVector = Icons.Outlined.MoreVert,
              contentDescription = "More",
              tint = textColor,
            )
          }

          viewState.mediaDetails?.let {
            DetailsDropdownMenu(
              mediaDetails = viewState.mediaDetails,
              expanded = showDropdownMenu,
              options = viewState.menuOptions,
              spoilersObfuscated = viewState.spoilersObfuscated,
              onDismissDropdown = { showDropdownMenu = false },
              onObfuscateClick = onObfuscateSpoilers,
            )
          }
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
            uiState = viewState,
            onNavigate = onNavigate,
            visibilityScope = animatedVisibilityScope,
            trailer = viewState.trailer,
            onMediaItemClick = onSimilarMovieClicked,
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
            onSwitchViewMode = onSwitchViewMode,
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
  uiState: DetailsViewState,
  visibilityScope: AnimatedVisibilityScope,
  onNavigate: (Navigation) -> Unit,
  trailer: Video?,
  obfuscateEpisodes: Boolean,
  onPersonClick: (Person) -> Unit,
  onMediaItemClick: (MediaItem.Media) -> Unit,
  onAddRateClick: () -> Unit,
  onAddToWatchlistClick: () -> Unit,
  viewAllCreditsClick: () -> Unit,
  viewAllRatingsClick: () -> Unit,
  onWatchTrailer: (String) -> Unit,
  onTabSelected: (Int) -> Unit,
  onShowTitle: (Float) -> Unit,
  onBackdropLoaded: () -> Unit,
  onOpenManageModal: () -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
  scope: CoroutineScope,
) {
  if (uiState.mediaDetails == null) return

  var selectedPage by rememberSaveable { mutableIntStateOf(uiState.selectedTabIndex) }
  val pagerState = rememberPagerState(
    initialPage = selectedPage,
    pageCount = { uiState.tabs.size },
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }
      .distinctUntilChanged()
      .collectLatest { page ->
        selectedPage = page
        onTabSelected(page)
      }
  }

  DynamicDetailsCollapsingToolbar(
    visibilityScope = visibilityScope,
    mediaDetails = uiState.mediaDetails,
    accountDataState = uiState.accountDataState,
    onNavigate = {
      onNavigate(it)
    },
    status = uiState.jellyseerrMediaInfo?.status,
    ratingSource = uiState.ratingSource,
    hasTrailer = trailer?.key != null,
    canManageRequests = uiState.canManageRequests,
    onAddToWatchlistClick = onAddToWatchlistClick,
    onAddRateClick = onAddRateClick,
    onShowAllRatingsClick = viewAllRatingsClick,
    userDetails = uiState.userDetails,
    onScrollProgress = onShowTitle,
    onWatchTrailerClick = { trailer?.key?.let { onWatchTrailer(it) } },
    onBackdropLoaded = onBackdropLoaded,
    onOpenManageModal = onOpenManageModal,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      ScenePeekTabs(
        tabs = uiState.tabs,
        selectedIndex = selectedPage,
        onClick = {
          scope.launch {
            pagerState.animateScrollToPage(it)
          }
        },
      )

      HorizontalPager(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background),
        state = pagerState,
      ) { page ->
        uiState.forms.values.elementAt(page).let { form ->
          when (form) {
            DetailsForm.Error -> Column(
              modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = MaterialTheme.dimensions.keyline_16)
                .verticalScroll(rememberScrollState()),
            ) {
              BlankSlate(uiState = BlankSlateState.Contact)
            }

            DetailsForm.Loading -> LoadingContent(
              modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            )

            is DetailsForm.Content<*> -> when (form.data) {
              is DetailsData.About -> AboutFormContent(
                modifier = Modifier.fillMaxSize(),
                aboutData = form.data as DetailsData.About,
                onPersonClick = onPersonClick,
                onGenreClick = {},
              )
              is DetailsData.Cast -> CastFormContent(
                modifier = Modifier.fillMaxSize(),
                cast = form.data as DetailsData.Cast,
                title = uiState.mediaDetails.title,
                onPersonClick = onPersonClick,
                obfuscateSpoilers = obfuscateEpisodes,
                onViewAllClick = viewAllCreditsClick,
              )
              is DetailsData.Recommendations -> RecommendationsFormContent(
                modifier = Modifier.fillMaxSize(),
                recommendations = form.data as DetailsData.Recommendations,
                title = uiState.mediaDetails.title,
                onSwitchViewMode = onSwitchViewMode,
                onItemClick = onMediaItemClick,
                onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
              )
              is DetailsData.Reviews -> ReviewsFormContent(
                modifier = Modifier.fillMaxSize(),
                title = uiState.mediaDetails.title,
                reviews = form.data as DetailsData.Reviews,
              )
              is DetailsData.Seasons -> SeasonsFormContent(
                modifier = Modifier.fillMaxSize(),
                title = uiState.mediaDetails.title,
                reviews = form.data as DetailsData.Seasons,
              )
            }
          }
        }
      }
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
      navigationProvider = emptyList(),
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
              onSimilarMovieClicked = {},
              onConsumeSnackbar = {},
              onAddRateClick = {},
              onAddToWatchlistClick = {},
              onPersonClick = {},
              onViewAllCreditsClick = {},
              onObfuscateSpoilers = {},
              onShowAllRatingsClick = {},
              onTabSelected = {},
              onPlayTrailerClick = {},
              onDeleteRequest = {},
              onDeleteMedia = {},
              onSwitchViewMode = {},
              onUpdateMediaInfo = {},
            )
          }
        }
      }
    }
  }
}
